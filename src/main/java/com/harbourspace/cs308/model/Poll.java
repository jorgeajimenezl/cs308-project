package com.harbourspace.cs308.model;

import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Table(indexes = @Index(columnList = "slug"))
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
