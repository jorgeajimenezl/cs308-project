package com.example.cs308.spring;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Review {
    private final int rating;
    private final String comment;
}
