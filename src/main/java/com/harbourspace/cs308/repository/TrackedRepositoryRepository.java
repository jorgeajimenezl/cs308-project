package com.harbourspace.cs308.repository;

import java.time.Instant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.harbourspace.cs308.model.TrackedRepository;

public interface TrackedRepositoryRepository extends JpaRepository<TrackedRepository, Long> {
    Page<TrackedRepository> findByActiveTrue(Pageable pageable);
    TrackedRepository findByOwnerAndName(String owner, String name);
}
