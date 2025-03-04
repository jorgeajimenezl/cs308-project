package com.harbourspace.cs308.controller.api;

import com.harbourspace.cs308.model.Subscriber;
import com.harbourspace.cs308.repository.SubscriberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
public class EmailApiController {

    @Autowired
    private SubscriberRepository repository;

    @PostMapping("/api/subscribe")
    public ResponseEntity<?> subscribeApi(@Valid @RequestBody Subscriber subscriber, BindingResult bindingResult,
                                          HttpServletRequest request) {
        if (repository.existsByEmail(subscriber.getEmail())) {
            bindingResult.rejectValue("email", "error.subscriber", "This email has already been registered.");
        }

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(err ->
                errors.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        subscriber.setSubscriptionTime(LocalDateTime.now());
        subscriber.setIpAddress(request.getRemoteAddr());
        repository.addSubscriber(subscriber);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Thank you for subscribing!");
        return ResponseEntity.ok(response);
    }
}