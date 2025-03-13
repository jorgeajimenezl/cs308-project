package com.harbourspace.cs308.model;

import java.util.List;
import java.time.Instant;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Entity
public class TrackedRepository extends BaseEntity {
    @Column(nullable = false)
    private String owner;
    
    @Column(nullable = false)
    private String name;

    private Boolean active = true;

    @Column(name = "last_tracked_time", nullable = false)
    private Instant lastTrackedTime;

    @OneToMany(mappedBy = "repository", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RepositoryActivity> activities;
}
