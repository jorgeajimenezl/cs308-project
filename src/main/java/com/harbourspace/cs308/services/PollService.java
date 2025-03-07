package com.harbourspace.cs308.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;

import com.harbourspace.cs308.dto.PollDto;
import com.harbourspace.cs308.mapper.PollMapper;
import com.harbourspace.cs308.model.Poll;
import com.harbourspace.cs308.repository.PollRepository;

@Service
public class PollService {
    @Autowired
    private PollRepository pollRepository;
    @Autowired
    private PollMapper pollMapper;

    public Page<Poll> getPolls(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return pollRepository.findByIsPublicTrue(pageable);
    }

    public Poll createPoll(PollDto createPollDto) {
        Poll poll = pollMapper.toPoll(createPollDto);
        return pollRepository.save(poll);
    }

    public PollDto getPoll(String slug) {
        Poll poll = pollRepository.findBySlug(slug);        
        return pollMapper.toPollDto(poll);
    }
}