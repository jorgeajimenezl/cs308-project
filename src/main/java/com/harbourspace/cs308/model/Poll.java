package com.harbourspace.cs308.model;

import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
public class Poll extends BaseEntity {
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
