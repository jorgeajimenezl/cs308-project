package com.harbourspace.cs308.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.harbourspace.cs308.model.Poll;

public interface PollRepository extends JpaRepository<Poll, Long> {
    Page<Poll> findByIsPublicTrue(Pageable pageable);
    
    Poll findBySlug(String slug);

    boolean existsBySlug(String slug);    
}