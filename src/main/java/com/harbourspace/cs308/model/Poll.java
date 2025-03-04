package com.harbourspace.cs308.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
public class Poll extends BaseEntity {
    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String question;

    @Column(nullable = false)
    private Boolean multipleChoice = false;

    @Column(nullable = false)
    private Boolean isPublic = false;

    @Column(unique = true, nullable = true)
    private String slug;

    @OneToMany(mappedBy = "poll")
    private List<PollOption> options;
}
