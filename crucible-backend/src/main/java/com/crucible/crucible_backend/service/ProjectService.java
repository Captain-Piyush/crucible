package com.crucible.crucible_backend.service;

import com.crucible.crucible_backend.dto.ProjectCreateRequest;
import com.crucible.crucible_backend.dto.ProjectResponse;
import com.crucible.crucible_backend.entity.Project;
import com.crucible.crucible_backend.entity.User;
import com.crucible.crucible_backend.repository.ProjectRepository;
import com.crucible.crucible_backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    // Dependency Injection: Spring automatically provides these repositories when the service is created
    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ProjectResponse createProject(ProjectCreateRequest request) {
        // 1. Fetch the user (the poster) from the database
        // (In a real app, we'd handle the "Not Found" exception gracefully, but we'll use a basic throw for now)
        User poster = userRepository.findById(request.getPosterId())
                .orElseThrow(() -> new RuntimeException("Poster not found"));

        // 2. Map the DTO to a new Project Entity
        Project project = new Project(
                request.getTitle(),
                request.getDescription(),
                request.getTotalBudget(),
                poster
        );

        // 3. Save to database
        Project savedProject = projectRepository.save(project);

        // 4. Map the saved Entity back to a Response DTO
        ProjectResponse response = new ProjectResponse();
        response.setId(savedProject.getId());
        response.setTitle(savedProject.getTitle());
        response.setDescription(savedProject.getDescription());
        response.setTotalBudget(savedProject.getTotalBudget());
        response.setPosterId(savedProject.getPoster().getId());

        return response;
    }
}