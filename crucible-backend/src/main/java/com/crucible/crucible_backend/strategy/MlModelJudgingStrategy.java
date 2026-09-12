package com.crucible.crucible_backend.strategy;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.util.Map;

@Component
public class MlModelJudgingStrategy implements JudgingStrategy {

    private final RestClient restClient = RestClient.builder().baseUrl("http://localhost:8000").build();

    @Override
    public boolean supports(String gigType) {
        return "ML_MODEL".equalsIgnoreCase(gigType);
    }

    @Override
    public Double evaluate(String deliverableReference) {
        try {
            Map<String, Object> response = restClient.post()
                    .uri("/evaluate-model")
                    // Note: You can expand this to extract a real dataset_id if your entity supports it
                    .body(Map.of("model_path", deliverableReference, "dataset_id", "default_dataset"))
                    .retrieve()
                    .body(Map.class);

            if (response != null && response.get("score") != null) {
                return Double.parseDouble(response.get("score").toString());
            }
            return 0.0;
        } catch (Exception e) {
            System.err.println("ML evaluation failed: " + e.getMessage());
            return 0.0;
        }
    }
}