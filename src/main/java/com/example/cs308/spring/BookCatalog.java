package com.example.cs308.spring;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookCatalog {
    private List<Book> books = new ArrayList<>();

    @Autowired
    private ReviewManager reviewManager;

    public void addBook(Book book) {
        books.add(book);
    }

    public boolean removeBook(Book book) {
        return books.remove(book);
    }

    public List<Book> getBooks() {
        return Collections.unmodifiableList(books);
    }
}
