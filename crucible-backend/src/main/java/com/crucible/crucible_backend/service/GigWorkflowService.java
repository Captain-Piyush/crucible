package com.crucible.crucible_backend.service;

import com.crucible.crucible_backend.entity.Gig;
import com.crucible.crucible_backend.entity.Submission;
import com.crucible.crucible_backend.repository.GigRepository;
import com.crucible.crucible_backend.repository.SubmissionRepository;
import com.crucible.escrow.service.EscrowService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class GigWorkflowService {

    private final SubmissionRepository submissionRepository;
    private final GigRepository gigRepository;
    private final StipendCalculationService stipendCalculationService;
    private final EscrowService escrowService;

    public GigWorkflowService(SubmissionRepository submissionRepository,
                              GigRepository gigRepository,
                              StipendCalculationService stipendCalculationService,
                              EscrowService escrowService) {
        this.submissionRepository = submissionRepository;
        this.gigRepository = gigRepository;
        this.stipendCalculationService = stipendCalculationService;
        this.escrowService = escrowService;
    }

    /**
     * Stage 1: Poster shortlists a submission, enforcing the maximum limit of 3 finalists.
     */
    @Transactional
    public Submission shortlistSubmission(Long submissionId) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new IllegalArgumentException("Submission not found with ID: " + submissionId));

        Gig gig = submission.getGig();

        long currentShortlistedCount = submissionRepository.countByGigIdAndIsShortlistedTrue(gig.getId());

        if (!submission.isShortlisted() && currentShortlistedCount >= 3) {
            throw new IllegalStateException("Cannot shortlist more than 3 finalists per gig.");
        }

        submission.setShortlisted(true);
        return submissionRepository.save(submission);
    }

    /**
     * Stage 2 & Settlement: Selects the winner, allocates runners-up, unlocks blockchain funds, and computes the FinancialLedger.
     */
    @Transactional
    public StipendCalculationService.FinancialLedger settleGig(Long gigId, Long winnerSubmissionId, List<Long> runnerUpSubmissionIds) {
        Gig gig = gigRepository.findById(gigId)
                .orElseThrow(() -> new IllegalArgumentException("Gig not found with ID: " + gigId));

        // 1. Validate and mark the winner
        Submission winnerSubmission = submissionRepository.findById(winnerSubmissionId)
                .orElseThrow(() -> new IllegalArgumentException("Winner submission not found."));

        if (!winnerSubmission.isShortlisted()) {
            throw new IllegalStateException("The winner must be one of the shortlisted finalists.");
        }
        winnerSubmission.setWinner(true);
        submissionRepository.save(winnerSubmission);

        // 2. Validate and mark runners-up
        if (runnerUpSubmissionIds != null && !runnerUpSubmissionIds.isEmpty()) {
            if (runnerUpSubmissionIds.size() > 3) {
                throw new IllegalArgumentException("Cannot have more than 3 runners-up.");
            }
            for (Long ruId : runnerUpSubmissionIds) {
                Submission ruSubmission = submissionRepository.findById(ruId)
                        .orElseThrow(() -> new IllegalArgumentException("Runner-up submission not found: " + ruId));
                if (!ruSubmission.isShortlisted()) {
                    throw new IllegalStateException("Runners-up must be selected from shortlisted finalists.");
                }
            }
        }

        int numberOfRunnersUp = (runnerUpSubmissionIds != null) ? runnerUpSubmissionIds.size() : 0;
        BigDecimal totalBudget = gig.getTotalBudget() != null ? gig.getTotalBudget() : BigDecimal.ZERO;

        // 3. Trigger Blockchain Payout (The Oracle Bridge)
        if (gig.getEscrowContractAddress() != null) {
            System.out.println("Executing on-chain settlement for Gig ID: " + gigId + " at contract: " + gig.getEscrowContractAddress());
            // This assumes your EscrowService has a releaseEscrow(address) method mapped to your Smart Contract
            escrowService.releaseEscrow(gig.getEscrowContractAddress());
        }

        // 4. Compute and return the final payout ledger
        return stipendCalculationService.calculatePayouts(totalBudget, numberOfRunnersUp);
    }
}