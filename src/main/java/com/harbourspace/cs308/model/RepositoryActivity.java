package com.harbourspace.cs308.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Entity
public class RepositoryActivity extends BaseEntity {
    @Embeddable
    public static class Author {
        private String name;
        private String email;
    }

    @Enumerated(EnumType.STRING)
    private RepositoryActivityType type;

    @Column(nullable = false)
    @Embedded
    private Author author;
    
    @Column(nullable = false)
    private String githubUrl;
    
    private String description;
    private Instant timestamp;

    @ManyToOne
    @JoinColumn(name = "repository_id", nullable = false)
    private TrackedRepository repository;
}
