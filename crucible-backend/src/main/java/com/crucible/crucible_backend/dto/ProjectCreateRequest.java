package com.crucible.crucible_backend.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ProjectCreateRequest {

    // We use Jakarta Validation annotations to protect our API
    @NotBlank(message = "Project title cannot be blank")
    private String title;

    @NotBlank(message = "Project description cannot be blank")
    private String description;

    @NotNull(message = "Total budget is required")
    @Positive(message = "Total budget must be greater than zero")
    private BigDecimal totalBudget;

    @NotNull(message = "Poster ID is required")
    private Long posterId;

    // --- Getters and Setters ---
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getTotalBudget() { return totalBudget; }
    public void setTotalBudget(BigDecimal totalBudget) { this.totalBudget = totalBudget; }
    public Long getPosterId() { return posterId; }
    public void setPosterId(Long posterId) { this.posterId = posterId; }
}