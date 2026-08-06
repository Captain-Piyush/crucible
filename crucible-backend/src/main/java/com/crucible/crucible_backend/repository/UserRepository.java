package com.crucible.crucible_backend.repository;

import com.crucible.crucible_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Spring gives us save(), findById(), findAll(), delete() for free!
}