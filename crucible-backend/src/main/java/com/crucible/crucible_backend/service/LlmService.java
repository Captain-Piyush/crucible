package com.crucible.crucible_backend.service;

import com.crucible.crucible_backend.dto.ProjectAnalysisDto;
import com.crucible.crucible_backend.entity.Project;
import com.crucible.crucible_backend.entity.ProjectModule;
import com.crucible.crucible_backend.entity.User;
import com.crucible.crucible_backend.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class LlmService {

    private final RestClient restClient;
    private final ProjectRepository projectRepository;

    public LlmService(ProjectRepository projectRepository) {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8000")
                .build();
        this.projectRepository = projectRepository;
    }

    public ProjectAnalysisDto analyzeProject(String userDescription) {
        return restClient.post()
                .uri("/generate-breakdown")
                .body(Map.of("user_description", userDescription))
                .retrieve()
                .body(ProjectAnalysisDto.class);
    }

    @Transactional
    public Project generateAndSaveProject(String userDescription, User poster, BigDecimal totalBudget) {
        ProjectAnalysisDto analysis = analyzeProject(userDescription);

        Project project = new Project(
                "AI Generated Project Breakdown",
                userDescription,
                totalBudget,
                poster
        );

        int sequence = 1;
        if (analysis.modules() != null) {
            for (var moduleDto : analysis.modules()) {

                // Perfectly aligned with your record fields
                ProjectModule module = new ProjectModule(
                        moduleDto.module_name(),
                        moduleDto.description(),
                        sequence++,
                        project
                );

                project.addModule(module);
            }
        }

        return projectRepository.save(project);
    }
}