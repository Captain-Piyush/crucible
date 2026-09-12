package com.crucible.crucible_backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    // The single total budget paid by the poster, as defined in the handoff
    @Column(nullable = false)
    private BigDecimal totalBudget;

    // Many projects can belong to One user (the poster)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poster_id", nullable = false)
    private User poster;

    // One project contains Many gigs.
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Gig> gigs = new ArrayList<>();

    // Maps the AI breakdowns directly to the parent project
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectModule> modules = new ArrayList<>();

    // --- Constructors ---
    public Project() {}

    public Project(String title, String description, BigDecimal totalBudget, User poster) {
        this.title = title;
        this.description = description;
        this.totalBudget = totalBudget;
        this.poster = poster;
    }

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getTotalBudget() { return totalBudget; }
    public void setTotalBudget(BigDecimal totalBudget) { this.totalBudget = totalBudget; }
    public User getPoster() { return poster; }
    public void setPoster(User poster) { this.poster = poster; }

    public List<Gig> getGigs() { return gigs; }
    public void setGigs(List<Gig> gigs) { this.gigs = gigs; }

    public List<ProjectModule> getModules() { return modules; }
    public void setModules(List<ProjectModule> modules) { this.modules = modules; }

    // Helper method to keep JPA relationships synchronized
    public void addModule(ProjectModule module) {
        modules.add(module);
        module.setProject(this);
    }
}