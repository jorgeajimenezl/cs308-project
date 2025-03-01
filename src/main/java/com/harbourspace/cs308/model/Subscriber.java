package com.harbourspace.cs308.model;

import java.time.LocalDateTime;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

public class Subscriber {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private LocalDateTime subscriptionTime;

    @Getter
    @Setter
    private String ipAddress;

    public Subscriber() {
    }

    public Subscriber(String email, LocalDateTime subscriptionTime, String ipAddress) {
        this.email = email;
        this.subscriptionTime = subscriptionTime;
        this.ipAddress = ipAddress;
    }
}
