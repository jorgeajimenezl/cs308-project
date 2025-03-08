package com.harbourspace.cs308.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.harbourspace.cs308.dto.PollDto;
import com.harbourspace.cs308.services.PollService;

@RestController
public class PollApiController {
    @Autowired
    private PollService pollService;

    @GetMapping("/api/polls")
    public ResponseEntity<Page<PollDto>> getPolls(Pageable pageable) {
        return ResponseEntity.ok(pollService.getPolls(pageable));
    }

    @PostMapping("/api/polls")
    public ResponseEntity<Void> createPoll(@RequestBody PollDto createPollDto) {
        pollService.createPoll(createPollDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/api/polls/{slug}")
    public ResponseEntity<PollDto> getPoll(@PathVariable String slug) {
        return ResponseEntity.ok(pollService.getPoll(slug));
    }
}