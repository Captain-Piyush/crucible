package com.crucible.crucible_backend.dto;

import jakarta.validation.constraints.NotBlank;

public class DeliverableSubmitRequest {

    @NotBlank(message = "Deliverable reference cannot be blank")
    private String deliverableReference;

    public String getDeliverableReference() { return deliverableReference; }
    public void setDeliverableReference(String deliverableReference) { this.deliverableReference = deliverableReference; }
}