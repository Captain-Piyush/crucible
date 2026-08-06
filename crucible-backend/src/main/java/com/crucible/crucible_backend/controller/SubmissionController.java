package com.crucible.crucible_backend.controller;

import com.crucible.crucible_backend.dto.DeliverableSubmitRequest;
import com.crucible.crucible_backend.dto.PitchCreateRequest;
import com.crucible.crucible_backend.dto.SubmissionResponse;
import com.crucible.crucible_backend.service.SubmissionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/submissions")
public class SubmissionController {

    private final SubmissionService submissionService;

    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @PostMapping("/pitch")
    public ResponseEntity<SubmissionResponse> submitPitch(@Valid @RequestBody PitchCreateRequest request) {
        return new ResponseEntity<>(submissionService.submitPitch(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/shortlist")
    public ResponseEntity<SubmissionResponse> shortlistSubmission(@PathVariable Long id) {
        return ResponseEntity.ok(submissionService.shortlistSubmission(id));
    }

    @PutMapping("/{id}/deliver")
    public ResponseEntity<SubmissionResponse> submitDeliverable(@PathVariable Long id, @Valid @RequestBody DeliverableSubmitRequest request) {
        return ResponseEntity.ok(submissionService.submitDeliverable(id, request.getDeliverableReference()));
    }

    @PutMapping("/{id}/winner")
    public ResponseEntity<SubmissionResponse> markAsWinner(@PathVariable Long id) {
        return ResponseEntity.ok(submissionService.markAsWinner(id));
    }
}