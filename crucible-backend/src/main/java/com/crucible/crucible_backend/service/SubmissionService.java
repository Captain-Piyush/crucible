package com.crucible.crucible_backend.service;

import com.crucible.crucible_backend.dto.PitchCreateRequest;
import com.crucible.crucible_backend.dto.SubmissionResponse;
import com.crucible.crucible_backend.entity.Gig;
import com.crucible.crucible_backend.entity.Submission;
import com.crucible.crucible_backend.entity.User;
import com.crucible.crucible_backend.repository.GigRepository;
import com.crucible.crucible_backend.repository.SubmissionRepository;
import com.crucible.crucible_backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final GigRepository gigRepository;
    private final UserRepository userRepository;

    public SubmissionService(SubmissionRepository submissionRepository, GigRepository gigRepository, UserRepository userRepository) {
        this.submissionRepository = submissionRepository;
        this.gigRepository = gigRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public SubmissionResponse submitPitch(PitchCreateRequest request) {
        Gig gig = gigRepository.findById(request.getGigId()).orElseThrow(() -> new RuntimeException("Gig not found"));
        User freelancer = userRepository.findById(request.getFreelancerId()).orElseThrow(() -> new RuntimeException("Freelancer not found"));
        Submission submission = new Submission(gig, freelancer, request.getPitch());
        return mapToResponse(submissionRepository.save(submission));
    }

    @Transactional
    public SubmissionResponse shortlistSubmission(Long submissionId) {
        Submission submission = submissionRepository.findById(submissionId).orElseThrow(() -> new RuntimeException("Submission not found"));
        submission.setShortlisted(true);
        return mapToResponse(submissionRepository.save(submission));
    }

    @Transactional
    public SubmissionResponse submitDeliverable(Long submissionId, String deliverableReference) {
        Submission submission = submissionRepository.findById(submissionId).orElseThrow(() -> new RuntimeException("Submission not found"));

        // Security Rule: Reject code if not shortlisted
        if (!submission.isShortlisted()) {
            throw new RuntimeException("Cannot submit deliverable: Pitch has not been shortlisted.");
        }

        submission.setDeliverableReference(deliverableReference);
        return mapToResponse(submissionRepository.save(submission));
    }

    @Transactional
    public SubmissionResponse markAsWinner(Long submissionId) {
        Submission submission = submissionRepository.findById(submissionId).orElseThrow(() -> new RuntimeException("Submission not found"));
        submission.setWinner(true);
        return mapToResponse(submissionRepository.save(submission));
    }

    // Helper method to keep code clean and prevent mapping errors
    private SubmissionResponse mapToResponse(Submission submission) {
        SubmissionResponse response = new SubmissionResponse();
        response.setId(submission.getId());
        response.setGigId(submission.getGig().getId());
        response.setFreelancerId(submission.getFreelancer().getId());
        response.setPitch(submission.getPitch());
        response.setShortlisted(submission.isShortlisted());
        response.setDeliverableReference(submission.getDeliverableReference());
        response.setWinner(submission.isWinner());
        return response;
    }
}