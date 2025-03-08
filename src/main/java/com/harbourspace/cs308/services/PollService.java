package com.harbourspace.cs308.services;

import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.harbourspace.cs308.dto.PollDto;
import com.harbourspace.cs308.mapper.PollMapper;
import com.harbourspace.cs308.model.Poll;
import com.harbourspace.cs308.repository.PollRepository;
import com.harbourspace.cs308.exceptions.EntityNotFoundException;

@Service
public class PollService {
    @Autowired
    private PollRepository pollRepository;
    @Autowired
    private PollMapper pollMapper;

    public Page<PollDto> getPolls(Pageable pageable) {
        Page<Poll> page = pollRepository.findByIsPublicTrue(pageable);

        return new PageImpl<>(
                page.getContent().stream().map(pollMapper::toPollDto).collect(Collectors.toList()),
                pageable, page.getTotalElements());
    }

    public Poll createPoll(PollDto createPollDto) {
        Poll poll = pollMapper.toPoll(createPollDto);
        return pollRepository.save(poll);
    }

    public String generateSlug() {
        String slug = UUID.randomUUID().toString();
        while (pollRepository.existsBySlug(slug)) {
            slug = UUID.randomUUID().toString();
        }
        return slug;
    }

    public PollDto getPoll(String slug) {
        Poll poll = pollRepository.findBySlug(slug);

        if (poll == null) {
            throw new EntityNotFoundException(Poll.class, slug);
        }

        return pollMapper.toPollDto(poll);
    }
}