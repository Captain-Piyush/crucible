package com.crucible.crucible_backend.dto;

import com.crucible.crucible_backend.entity.GigType;
import java.math.BigDecimal;

public class GigResponse {

    private Long id;
    private String title;
    private String description;
    private GigType gigType;
    private BigDecimal budget;
    private Long projectId;

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public GigType getGigType() { return gigType; }
    public void setGigType(GigType gigType) { this.gigType = gigType; }
    public BigDecimal getBudget() { return budget; }
    public void setBudget(BigDecimal budget) { this.budget = budget; }
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
}