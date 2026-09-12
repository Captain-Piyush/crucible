from fastapi import FastAPI, HTTPException, Request, Response
from starlette.middleware.base import BaseHTTPMiddleware
from pydantic import BaseModel, Field
from typing import List, Optional
import joblib
import numpy as np
import pandas as pd
from sklearn.metrics import mean_squared_error, r2_score, accuracy_score, f1_score
from google import genai
from google.genai import types
import json
import os
from dotenv import load_dotenv

load_dotenv()

app = FastAPI(title="Crucible ML & AI Microservice")

# --- Security: OOM / DoS Protection Middleware ---
class LimitUploadSize(BaseHTTPMiddleware):
    def __init__(self, app, max_upload_size: int):
        super().__init__(app)
        self.max_upload_size = max_upload_size

    async def dispatch(self, request: Request, call_next):
        if request.method == "POST" and request.url.path == "/evaluate-model":
            if "content-length" in request.headers:
                content_length = int(request.headers["content-length"])
                if content_length > self.max_upload_size:
                    return Response(status_code=413, content="Payload Too Large. Max size is 500MB.")
        return await call_next(request)

# Limit uploads to 500 MB (500 * 1024 * 1024 bytes)
app.add_middleware(LimitUploadSize, max_upload_size=524288000)

DATASETS_DIR = os.getenv("DATASETS_DIR", os.path.join(os.path.dirname(__file__), "datasets"))
os.makedirs(DATASETS_DIR, exist_ok=True)


# --- Phase 3: Kaggle-Style Evaluation Engine ---
class EvaluationRequest(BaseModel):
    model_path: str
    dataset_id: str
    task_type: Optional[str] = "regression"
    target_column: Optional[str] = "target"

@app.post("/evaluate-model")
async def evaluate_model(request: EvaluationRequest):
    ground_truth_path = os.path.join(DATASETS_DIR, f"{request.dataset_id}_test.csv")
    if not os.path.exists(ground_truth_path):
        raise HTTPException(
            status_code=404,
            detail=f"Held-out dataset '{request.dataset_id}' not found at {ground_truth_path}"
        )

    if not os.path.exists(request.model_path):
        raise HTTPException(
            status_code=404,
            detail=f"Submitted model not found at {request.model_path}"
        )

    try:
        # Load the model directly from the filepath provided by the Spring Boot backend
        submitted_model = joblib.load(request.model_path)
        df_test = pd.read_csv(ground_truth_path)

        # Dynamic fallback: Use specified column, or default to the last column in the CSV
        target = request.target_column if request.target_column in df_test.columns else df_test.columns[-1]

        X_test = df_test.drop(columns=[target])
        y_test = df_test[target]

        y_pred = submitted_model.predict(X_test)

        if request.task_type == "regression":
            rmse = float(np.sqrt(mean_squared_error(y_test, y_pred)))
            r2 = float(r2_score(y_test, y_pred))
            score = max(0.0, min(100.0, r2 * 100)) # Score mapped to 0 - 100
            return {
                "gig_id": request.dataset_id,
                "score": round(score, 2),
                "primary_metric": "R2",
                "details": {"r2_score": round(r2, 4), "rmse": round(rmse, 4)}
            }
        elif request.task_type == "classification":
            acc = float(accuracy_score(y_test, y_pred))
            f1 = float(f1_score(y_test, y_pred, average="weighted"))
            return {
                "gig_id": request.dataset_id,
                "score": round(acc * 100, 2),
                "primary_metric": "Accuracy",
                "details": {"accuracy": round(acc, 4), "f1_score": round(f1, 4)}
            }
        else:
            raise HTTPException(status_code=400, detail="Invalid task_type. Use 'regression' or 'classification'.")

    except Exception as e:
        raise HTTPException(status_code=422, detail=f"Model evaluation failed: {str(e)}")


# --- Phase 5: Gemini LLM Breakdown Assistant ---
client = genai.Client()

class ProjectPrompt(BaseModel):
    user_description: str

class ClarifyingQuestion(BaseModel):
    question: str
    options: List[str]

class ModuleBreakdown(BaseModel):
    module_name: str
    description: str
    priority: int

class ProjectAnalysis(BaseModel):
    is_clear: bool = Field(description="True if project is specific enough to build modules. False if underspecified.")
    clarifying_questions: List[ClarifyingQuestion] = Field(description="Max 2 bounded questions if is_clear is False.")
    modules: List[ModuleBreakdown] = Field(description="Structured gigs if is_clear is True. Empty if False.")

@app.post("/generate-breakdown", response_model=ProjectAnalysis)
def generate_breakdown(prompt: ProjectPrompt):
    system_instruction = """
    You are an expert software architect for 'Crucible', a gig marketplace.
    Evaluate the user's project description. If it is vague (e.g., "build me an app"), 
    set is_clear to false and provide 1-2 bounded clarifying questions (e.g., multiple choice) 
    regarding scale, auth, or integrations. 
    If it is specific enough to build, set is_clear to true and output the modular gigs 
    (Backend API, Frontend, Database, Smart Contracts, ML/Model). Do NOT include pricing.
    """

    response = client.models.generate_content(
        model='gemini-3.6-flash',
        contents=prompt.user_description,
        config=types.GenerateContentConfig(
            system_instruction=system_instruction,
            response_mime_type="application/json",
            response_schema=ProjectAnalysis,
            temperature=0.2,
        ),
    )

    return json.loads(response.text)