package com.crucible.crucible_backend.repository;

import com.crucible.crucible_backend.entity.Gig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GigRepository extends JpaRepository<Gig, Long> {
}