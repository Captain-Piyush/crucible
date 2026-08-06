package com.crucible.crucible_backend.repository;

import com.crucible.crucible_backend.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    // Custom query to find all submissions for a specific gig
    List<Submission> findByGigId(Long gigId);

    // Custom query to enforce the capped shortlist rule (maximum 3 finalists)
    long countByGigIdAndIsShortlistedTrue(Long gigId);
}