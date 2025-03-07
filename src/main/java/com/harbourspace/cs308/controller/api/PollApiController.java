package com.harbourspace.cs308.controller.api;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.harbourspace.cs308.dto.PollDto;
import com.harbourspace.cs308.model.Poll;
import com.harbourspace.cs308.services.PollService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PollApiController {
    private final PollService pollService;

    @GetMapping("/api/polls")
    public Page<Poll> getPolls(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        return pollService.getPolls(page, size, sortBy, sortDir);
    }

    @PostMapping("/api/polls")
    public ResponseEntity<Void> createPoll(@RequestBody PollDto createPollDto) {
        pollService.createPoll(createPollDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/api/polls/{slug}")
    public PollDto getPoll(@PathVariable String slug) {
        return pollService.getPoll(slug);
    }
}