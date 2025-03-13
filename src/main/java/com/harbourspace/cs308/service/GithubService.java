package com.harbourspace.cs308.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;

import com.harbourspace.cs308.dto.RepositoryActivityDto;
import com.harbourspace.cs308.exception.EntityNotFoundException;
import com.harbourspace.cs308.model.RepositoryActivity;
import com.harbourspace.cs308.model.TrackedRepository;
import com.harbourspace.cs308.repository.RepositoryActivityRepository;
import com.harbourspace.cs308.repository.TrackedRepositoryRepository;

import reactor.core.publisher.Flux;

@Service
public class GithubService {
    @Autowired
    private TrackedRepositoryRepository trackedRepository;

    @Autowired
    private RepositoryActivityRepository activityRepository;

    public void subscribe(String owner, String name) {
        TrackedRepository repo = new TrackedRepository(
                owner,
                name,
                true,
                Instant.EPOCH,
                null);
        trackedRepository.save(repo);
    }

    public Page<RepositoryActivityDto> getRecentActivities(String owner, String name, Instant since,
            Pageable pageable) {
        TrackedRepository repo = trackedRepository.findByOwnerAndName(owner, name);

        if (repo == null) {
            throw new EntityNotFoundException("Repository not found");
        }

        Page<RepositoryActivity> activities = activityRepository.findByRepositoryIdAndTimestampAfter(
            repo.getId(),
            since,
            pageable
        );

        return activities.map(activity -> new RepositoryActivityDto(
            String.format("%s/%s", owner, name),
            activity.getType(),
            activity.getDescription(),
            LocalDateTime.ofInstant(activity.getTimestamp(), ZoneId.of("UTC")).toString()
        ));
    }

    // public Flux<ServerSentEvent<RepositoryActivityDto>> listenActivities(String
    // owner, String name) {

    // }
}
