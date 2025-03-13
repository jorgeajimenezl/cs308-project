package com.harbourspace.cs308.repository;

import java.time.Instant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.harbourspace.cs308.model.RepositoryActivity;

public interface RepositoryActivityRepository extends JpaRepository<RepositoryActivity, Long> {
    Page<RepositoryActivity> findByRepositoryIdAndTimestampAfter(Long repositoryId, Instant timestamp, Pageable pageable);
}