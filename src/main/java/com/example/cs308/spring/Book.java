package com.example.cs308.spring;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Book {
    private final String title;
    private final String author;
}
