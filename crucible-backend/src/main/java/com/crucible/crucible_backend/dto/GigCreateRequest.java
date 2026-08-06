package com.crucible.crucible_backend.dto;

import com.crucible.crucible_backend.entity.GigType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class GigCreateRequest {

    @NotBlank(message = "Gig title cannot be blank")
    private String title;

    @NotBlank(message = "Gig description cannot be blank")
    private String description;

    @NotNull(message = "Gig type is required")
    private GigType gigType;

    @NotNull(message = "Project ID is required")
    private Long projectId;

    // Notice we do NOT use @NotNull here.
    // If the frontend leaves this null, our backend heuristic takes over!
    @Positive(message = "Budget must be positive")
    private BigDecimal budget;

    // --- Getters and Setters ---
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public GigType getGigType() { return gigType; }
    public void setGigType(GigType gigType) { this.gigType = gigType; }
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public BigDecimal getBudget() { return budget; }
    public void setBudget(BigDecimal budget) { this.budget = budget; }
}