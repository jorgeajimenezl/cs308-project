package com.harbourspace.cs308.repository;

import org.springframework.data.repository.CrudRepository;

import com.harbourspace.cs308.model.EmailSubscriber;

public interface EmailSubscriberRepository extends CrudRepository<EmailSubscriber, Long> {
    EmailSubscriber findByEmail(String email);
    boolean existsByEmail(String email);
}