package com.crucible.crucible_backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "project_modules")
public class ProjectModule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    // Retains the AI's suggested logical order for frontend rendering
    @Column(name = "sequence_order", nullable = false)
    private Integer sequenceOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    public ProjectModule() {}

    public ProjectModule(String title, String description, Integer sequenceOrder, Project project) {
        this.title = title;
        this.description = description;
        this.sequenceOrder = sequenceOrder;
        this.project = project;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getSequenceOrder() { return sequenceOrder; }
    public void setSequenceOrder(Integer sequenceOrder) { this.sequenceOrder = sequenceOrder; }

    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }
}