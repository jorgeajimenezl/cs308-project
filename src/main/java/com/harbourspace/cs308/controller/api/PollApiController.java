package com.harbourspace.cs308.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import com.harbourspace.cs308.model.Poll;
import com.harbourspace.cs308.repository.PollRepository;

@RestController
public class PollApiController {
    @Autowired
    private PollRepository pollRepository;

    @GetMapping("/api/polls")
    public Page<Poll> getPolls(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("asc") ? 
                Sort.by(sortBy).ascending() : 
                Sort.by(sortBy).descending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        return pollRepository.findByIsPublicTrue(pageable);
    }

    @PostMapping("/api/polls")
    public Poll createPoll(@RequestBody Poll poll) {
        return pollRepository.save(poll);
    }
}