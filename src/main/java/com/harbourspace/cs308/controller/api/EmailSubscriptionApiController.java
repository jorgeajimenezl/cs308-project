package com.harbourspace.cs308.controller.api;

import com.harbourspace.cs308.exception.FieldValidationException;
import com.harbourspace.cs308.model.EmailSubscriber;
import com.harbourspace.cs308.repository.EmailSubscriberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class EmailSubscriptionApiController {
    private final EmailSubscriberRepository repository;

    @PostMapping("/api/email-subscribe")
    public ResponseEntity<?> subscribeApi(@Valid @RequestBody EmailSubscriber subscriber, HttpServletRequest request) {
        if (repository.existsByEmail(subscriber.getEmail())) {
            throw new FieldValidationException("email", "This email has already been registered.");
        }

        subscriber.setIpAddress(request.getRemoteAddr());
        repository.save(subscriber);
        return ResponseEntity.ok().build();
    }
}