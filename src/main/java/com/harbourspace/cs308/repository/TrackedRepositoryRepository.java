package com.harbourspace.cs308.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.harbourspace.cs308.model.TrackedRepository;

public interface TrackedRepositoryRepository extends JpaRepository<TrackedRepository, Long> { }
