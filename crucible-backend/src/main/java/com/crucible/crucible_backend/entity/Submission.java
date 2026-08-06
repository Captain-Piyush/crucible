package com.crucible.crucible_backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "submissions")
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many submissions can be made for One gig
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gig_id", nullable = false)
    private Gig gig;

    // Many submissions can be made by One freelancer
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "freelancer_id", nullable = false)
    private User freelancer;

    // Stage 1: The initial pitch
    @Column(columnDefinition = "TEXT", nullable = false)
    private String pitch;

    // True if the poster shortlists them for Stage 2
    @Column(nullable = false)
    private boolean isShortlisted = false;

    // Stage 2: The final deliverable (e.g., a GitHub URL or a Docker image tag)
    // This is nullable because it doesn't exist during Stage 1
    @Column(columnDefinition = "TEXT")
    private String deliverableReference;

    // The score assigned by the Sandbox Test-Runner (e.g., 0-100 or pass/fail)
    private Double automatedScore;

    // True if the poster picks this as the absolute winner
    @Column(nullable = false)
    private boolean isWinner = false;

    // --- Timestamps for Escrow & Audit Trail ---
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    // --- Constructors ---
    public Submission() {}

    public Submission(Gig gig, User freelancer, String pitch) {
        this.gig = gig;
        this.freelancer = freelancer;
        this.pitch = pitch;
    }

    // --- Lifecycle Callbacks ---
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Gig getGig() { return gig; }
    public void setGig(Gig gig) { this.gig = gig; }
    public User getFreelancer() { return freelancer; }
    public void setFreelancer(User freelancer) { this.freelancer = freelancer; }
    public String getPitch() { return pitch; }
    public void setPitch(String pitch) { this.pitch = pitch; }
    public boolean isShortlisted() { return isShortlisted; }
    public void setShortlisted(boolean shortlisted) { this.isShortlisted = shortlisted; }
    public String getDeliverableReference() { return deliverableReference; }
    public void setDeliverableReference(String deliverableReference) { this.deliverableReference = deliverableReference; }
    public Double getAutomatedScore() { return automatedScore; }
    public void setAutomatedScore(Double automatedScore) { this.automatedScore = automatedScore; }
    public boolean isWinner() { return isWinner; }
    public void setWinner(boolean winner) { this.isWinner = winner; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}