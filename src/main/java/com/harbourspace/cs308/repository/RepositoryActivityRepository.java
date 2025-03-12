package com.harbourspace.cs308.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.harbourspace.cs308.model.RepositoryActivity;

public interface RepositoryActivityRepository extends JpaRepository<RepositoryActivity, Long> {}