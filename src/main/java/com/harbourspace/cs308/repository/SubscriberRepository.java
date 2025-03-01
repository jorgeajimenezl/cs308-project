package com.harbourspace.cs308.repository;

import com.harbourspace.cs308.model.Subscriber;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SubscriberRepository {

    private final List<Subscriber> subscribers = new ArrayList<>();

    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    public boolean existsByEmail(String email) {
        return subscribers.stream()
                          .anyMatch(sub -> sub.getEmail().equalsIgnoreCase(email));
    }

    public List<Subscriber> findAll() {
        return new ArrayList<>(subscribers);
    }
}
