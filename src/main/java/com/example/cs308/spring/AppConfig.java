package com.example.cs308.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.example.cs308.spring")
public class AppConfig {
    @Bean
    public Book book1() {
        System.out.println("Creating book1 bean");
        return new Book("Harry Potter and Soccers stone", "J. K. Rowling");
    }

    @Bean
    public Book book2() {
        System.out.println("Creating book2 bean");
        return new Book("Harry Potter and the Chamber of Secrets", "J. K. Rowling");
    }

    @Bean
    public Book book3() {
        System.out.println("Creating book3 bean");
        return new Book("Harry Potter and the Prisoner of Azkaban", "J. K. Rowling");
    }

    @Bean
    public Review review1() {
        System.out.println("Creating review1 bean");
        return new Review(5, "Excellent book, I enjoyed it a lot.");
    }

    @Bean
    public Review review2() {
        System.out.println("Creating review2 bean");
        return new Review(4, "A good read, I liked it.");
    }

    @Bean
    public DataInitializer dataInitializer(
            BookCatalog bookCatalog,
            ReviewManager reviewManager) {
        System.out.println("Creating dataInitializer bean");
        DataInitializer initializer = new DataInitializer(bookCatalog, reviewManager);
        initializer.initData(book1(), book2(), book3(), review1(), review2());
        return initializer;
    }
}
