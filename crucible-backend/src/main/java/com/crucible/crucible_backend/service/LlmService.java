package com.crucible.crucible_backend.service;

import com.crucible.crucible_backend.dto.ProjectAnalysisDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.util.Map;

@Service
public class LlmService {

    private final RestClient restClient;

    public LlmService() {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8000")
                .build();
    }

    public ProjectAnalysisDto analyzeProject(String userDescription) {
        return restClient.post()
                .uri("/generate-breakdown")
                .body(Map.of("user_description", userDescription))
                .retrieve()
                .body(ProjectAnalysisDto.class);
    }
}