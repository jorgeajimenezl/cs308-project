package com.harbourspace.cs308.controller.api;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.harbourspace.cs308.dto.PollDto;
import com.harbourspace.cs308.model.Poll;
import com.harbourspace.cs308.services.PollService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PollApiController {
    private final PollService pollService;

    @GetMapping("/api/polls")
    public ResponseEntity<Page<PollDto>> getPolls(@Valid Pageable pageable) {
        return ResponseEntity.ok(pollService.getPolls(pageable));
    }

    @PostMapping("/api/polls")
    public ResponseEntity<Void> createPoll(@Valid @RequestBody PollDto pollDto) {
        Poll poll = pollService.createPoll(pollDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{slug}")
                .buildAndExpand(poll.getSlug())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/api/polls/{slug}")
    public ResponseEntity<PollDto> getPoll(@PathVariable String slug) {
        return ResponseEntity.ok(pollService.getPoll(slug));
    }
}