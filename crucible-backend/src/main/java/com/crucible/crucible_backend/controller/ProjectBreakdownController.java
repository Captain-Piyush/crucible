package com.crucible.crucible_backend.controller;

import com.crucible.crucible_backend.dto.ProjectAnalysisDto;
import com.crucible.crucible_backend.service.LlmService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/projects/breakdown")
@CrossOrigin(origins = "http://localhost:5173") // Required for React/Vite integration
public class ProjectBreakdownController {

    private final LlmService llmService;

    // Spring Boot automatically injects the LlmService here (Dependency Injection)
    public ProjectBreakdownController(LlmService llmService) {
        this.llmService = llmService;
    }

    @PostMapping
    public ResponseEntity<ProjectAnalysisDto> analyze(@RequestBody Map<String, String> request) {
        String description = request.get("user_description");

        // Basic input validation
        if (description == null || description.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Calls the service, which calls Python, which calls Gemini
        return ResponseEntity.ok(llmService.analyzeProject(description));
    }
}