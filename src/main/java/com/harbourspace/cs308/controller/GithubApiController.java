package com.harbourspace.cs308.controller;

import org.springframework.web.bind.annotation.RestController;

import com.harbourspace.cs308.dto.RepositoryActivityDto;
import com.harbourspace.cs308.dto.RepositoryDto;
import com.harbourspace.cs308.service.GithubService;

import jakarta.validation.Valid;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class GithubApiController {
    @Autowired
    private GithubService githubService;

    @PostMapping("/api/github/{owner}/{name}/subscribe")
    public ResponseEntity<Void> subscribe(
        @PathVariable String owner,
        @PathVariable String name
    ) {
        githubService.subscribe(owner, name);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/github/{owner}/{name}/unsubscribe")
    public ResponseEntity<Void> unsubscribe(
        @PathVariable String owner,
        @PathVariable String name
    ) {
        // TODO: Implement this
        return ResponseEntity.status(501).build();
    }

    @GetMapping("/api/github/{owner}/{name}/activity")
    public ResponseEntity<Page<RepositoryActivityDto>> getRepositoryActivity(
        @PathVariable String owner,
        @PathVariable String name,
        @RequestParam(defaultValue = "1970-01-01T00:00:00Z") Instant since,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int pageSize
    ) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<RepositoryActivityDto> activities = githubService.getRecentActivities(owner, name, since, pageable);
        return ResponseEntity.ok(activities);
    }
}
