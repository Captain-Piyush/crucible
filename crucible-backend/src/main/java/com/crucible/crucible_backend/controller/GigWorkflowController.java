package com.crucible.crucible_backend.controller;

import com.crucible.crucible_backend.entity.Submission;
import com.crucible.crucible_backend.service.GigWorkflowService;
import com.crucible.crucible_backend.service.StipendCalculationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gigs")
public class GigWorkflowController {

    private final GigWorkflowService gigWorkflowService;

    public GigWorkflowController(GigWorkflowService gigWorkflowService) {
        this.gigWorkflowService = gigWorkflowService;
    }

    /**
     * Stage 1: Poster shortlists a submission (Enforces max 3 finalists).
     * Endpoint: PATCH /api/gigs/submissions/{submissionId}/shortlist
     */
    @PatchMapping("/submissions/{submissionId}/shortlist")
    public ResponseEntity<Submission> shortlistSubmission(@PathVariable Long submissionId) {
        Submission updatedSubmission = gigWorkflowService.shortlistSubmission(submissionId);
        return ResponseEntity.ok(updatedSubmission);
    }

    /**
     * Stage 2 & Settlement: Selects the winner, sets runners-up, and returns the exact FinancialLedger.
     * Endpoint: POST /api/gigs/{gigId}/settle?winnerSubmissionId=1
     * Body (Optional): List of runner-up submission IDs [2, 3]
     */
    @PostMapping("/{gigId}/settle")
    public ResponseEntity<StipendCalculationService.FinancialLedger> settleGig(
            @PathVariable Long gigId,
            @RequestParam Long winnerSubmissionId,
            @RequestBody(required = false) List<Long> runnerUpSubmissionIds) {

        StipendCalculationService.FinancialLedger ledger = gigWorkflowService.settleGig(
                gigId, winnerSubmissionId, runnerUpSubmissionIds
        );
        return ResponseEntity.ok(ledger);
    }
}