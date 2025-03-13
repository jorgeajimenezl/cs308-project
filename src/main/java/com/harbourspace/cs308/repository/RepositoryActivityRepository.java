package com.harbourspace.cs308.repository;

import java.time.Instant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.harbourspace.cs308.model.RepositoryActivity;
import com.harbourspace.cs308.model.RepositoryActivityType;

public interface RepositoryActivityRepository extends JpaRepository<RepositoryActivity, Long> {
    @Query("SELECT a FROM RepositoryActivity a WHERE a.repository.id = ?1 AND a.timestamp > ?2")
    Page<RepositoryActivity> findByRepositoryIdAndTimestampAfter(Long repositoryId, Instant timestamp, Pageable pageable);
    Long countByType(RepositoryActivityType type);
}