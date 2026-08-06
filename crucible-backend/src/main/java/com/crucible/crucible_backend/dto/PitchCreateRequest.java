package com.crucible.crucible_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PitchCreateRequest {

    @NotNull(message = "Gig ID is required")
    private Long gigId;

    @NotNull(message = "Freelancer ID is required")
    private Long freelancerId;

    @NotBlank(message = "Pitch cannot be blank")
    private String pitch;

    public Long getGigId() { return gigId; }
    public void setGigId(Long gigId) { this.gigId = gigId; }
    public Long getFreelancerId() { return freelancerId; }
    public void setFreelancerId(Long freelancerId) { this.freelancerId = freelancerId; }
    public String getPitch() { return pitch; }
    public void setPitch(String pitch) { this.pitch = pitch; }
}