package com.crucible.crucible_backend.service;

import com.crucible.crucible_backend.entity.Submission;
import com.crucible.crucible_backend.repository.SubmissionRepository;
import com.crucible.crucible_backend.strategy.JudgingStrategy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubmissionScoringService {

    private final List<JudgingStrategy> strategies;
    private final SubmissionRepository submissionRepository;

    public SubmissionScoringService(List<JudgingStrategy> strategies, SubmissionRepository submissionRepository) {
        this.strategies = strategies;
        this.submissionRepository = submissionRepository;
    }

    public void scoreSubmission(Long submissionId) {
        // 1. Fetch the actual submission
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found with ID: " + submissionId));

        // Ensure it's a Stage 2 submission with actual code to test
        if (submission.getDeliverableReference() == null || submission.getDeliverableReference().isEmpty()) {
            throw new IllegalStateException("Cannot score a Stage 1 submission. No deliverable reference found.");
        }

        // 2. Dynamically extract the Gig Type based on your relationship mapping
        // Note: Adjust .name() or .toString() depending on if your GigType is an Enum or String
        String gigType = submission.getGig().getType().name();

        // 3. Find the right strategy
        JudgingStrategy strategy = strategies.stream()
                .filter(s -> s.supports(gigType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No judging strategy found for type: " + gigType));

        // 4. Spin up Docker and get the Double score
        Double score = strategy.evaluate(submission.getDeliverableReference());

        // 5. Save back to your DB
        submission.setAutomatedScore(score);
        submissionRepository.save(submission);

        System.out.println("Scoring complete for Submission " + submissionId + " - Score: " + score);
    }
}