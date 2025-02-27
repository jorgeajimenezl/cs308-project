package com.example.cs308.spring;

public class DataInitializer {
    private final BookCatalog bookCatalog;
    private final ReviewManager reviewManager;

    public DataInitializer(BookCatalog bookCatalog, ReviewManager reviewManager) {
        this.bookCatalog = bookCatalog;
        this.reviewManager = reviewManager;
    }

    public void initData(Book book1, Book book2, Book book3, Review review1, Review review2) {
        // Add books to the catalog
        bookCatalog.addBook(book1);
        bookCatalog.addBook(book2);
        bookCatalog.addBook(book3);

        // Add reviews to the review manager
        reviewManager.addReview(review1);
        reviewManager.addReview(review2);

        System.out.println("Data initialization complete.");
    }
}
