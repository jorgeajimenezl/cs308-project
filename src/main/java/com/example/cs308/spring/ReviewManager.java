package com.example.cs308.spring;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ReviewManager {
    private List<Review> reviews = new ArrayList<>();

    private Book sampleBook;

    public void addReview(Review review) {
        reviews.add(review);
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public Book getSampleBook() {
        return sampleBook;
    }

    @Autowired
    public void setSampleBook(@Qualifier("book1") Book sampleBook) {
        this.sampleBook = sampleBook;
    }
}
