package com.crucible.crucible_backend.dto;

public class SubmissionResponse {
    private Long id;
    private Long gigId;
    private Long freelancerId;
    private String pitch;
    private boolean isShortlisted;
    private String deliverableReference;
    private boolean isWinner;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getGigId() { return gigId; }
    public void setGigId(Long gigId) { this.gigId = gigId; }
    public Long getFreelancerId() { return freelancerId; }
    public void setFreelancerId(Long freelancerId) { this.freelancerId = freelancerId; }
    public String getPitch() { return pitch; }
    public void setPitch(String pitch) { this.pitch = pitch; }
    public boolean isShortlisted() { return isShortlisted; }
    public void setShortlisted(boolean shortlisted) { this.isShortlisted = shortlisted; }
    public String getDeliverableReference() { return deliverableReference; }
    public void setDeliverableReference(String deliverableReference) { this.deliverableReference = deliverableReference; }
    public boolean isWinner() { return isWinner; }
    public void setWinner(boolean winner) { this.isWinner = winner; }
}