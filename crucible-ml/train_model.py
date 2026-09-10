import pandas as pd
import numpy as np
from sklearn.ensemble import RandomForestRegressor
from sklearn.model_selection import train_test_split
import joblib

# 1. Generate Synthetic Training Data (Simulating historical gig platform data)
np.random.seed(42)
num_samples = 500

data = {
    "description_length": np.random.randint(100, 2000, num_samples),
    "estimated_hours": np.random.uniform(5.0, 160.0, num_samples),
    "complexity_score": np.random.randint(1, 6, num_samples)
}
df = pd.DataFrame(data)

# Baseline formula with added noise to simulate real-world pricing variance
base_rate = 25.0
df["budget"] = (df["estimated_hours"] * base_rate) * (1 + (df["complexity_score"] * 0.15))
df["budget"] += np.random.normal(0, df["budget"] * 0.1) # Add 10% random noise

# 2. Train the Model
X = df[["description_length", "estimated_hours", "complexity_score"]]
y = df["budget"]

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

model = RandomForestRegressor(n_estimators=100, random_state=42)
model.fit(X_train, y_train)

print(f"Model Training R^2 Score: {model.score(X_test, y_test):.2f}")

# 3. Serialize and Save the Model
joblib.dump(model, "budget_model.pkl")
print("Model saved to budget_model.pkl")