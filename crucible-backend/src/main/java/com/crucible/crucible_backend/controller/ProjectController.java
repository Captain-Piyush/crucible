package com.crucible.crucible_backend.controller;

import com.crucible.crucible_backend.dto.ProjectCreateRequest;
import com.crucible.crucible_backend.dto.ProjectResponse;
import com.crucible.crucible_backend.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // The @Valid annotation triggers the @NotBlank and @NotNull checks inside our DTO
    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@Valid @RequestBody ProjectCreateRequest request) {
        ProjectResponse createdProject = projectService.createProject(request);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }
}