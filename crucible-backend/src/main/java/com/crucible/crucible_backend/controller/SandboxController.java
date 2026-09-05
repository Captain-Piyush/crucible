package com.crucible.crucible_backend.controller;

import com.crucible.crucible_backend.service.SubmissionScoringService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sandbox")
public class SandboxController {

    private final SubmissionScoringService scoringService;

    public SandboxController(SubmissionScoringService scoringService) {
        this.scoringService = scoringService;
    }

    @PostMapping("/trigger/{submissionId}")
    public ResponseEntity<String> triggerSandbox(@PathVariable Long submissionId) {
        scoringService.scoreSubmission(submissionId);
        return ResponseEntity.ok("Sandbox triggered and score saved for submission: " + submissionId);
    }
}