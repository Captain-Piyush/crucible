package com.crucible.crucible_backend.dto;

import java.math.BigDecimal;

public class ProjectResponse {

    private Long id;
    private String title;
    private String description;
    private BigDecimal totalBudget;
    private Long posterId;

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getTotalBudget() { return totalBudget; }
    public void setTotalBudget(BigDecimal totalBudget) { this.totalBudget = totalBudget; }
    public Long getPosterId() { return posterId; }
    public void setPosterId(Long posterId) { this.posterId = posterId; }
}