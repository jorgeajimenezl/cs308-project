package com.harbourspace.cs308.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harbourspace.cs308.model.TrackedRepository;
import com.harbourspace.cs308.repository.TrackedRepositoryRepository;

@Service
public class GithubService {
    @Autowired
    private TrackedRepositoryRepository trackedRepository;

    public void trackRepository(String owner, String name) {
        TrackedRepository repo = new TrackedRepository(
            owner, 
            name, 
            true, 
            Instant.EPOCH, 
            null
        );
        trackedRepository.save(repo);
    }

    public void getActivities() {
        
    }
}
