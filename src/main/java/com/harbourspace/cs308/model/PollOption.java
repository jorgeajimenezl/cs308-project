package com.harbourspace.cs308.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
public class PollOption extends BaseEntity {
    @Column(nullable = false)
    private String option;

    @ManyToOne
    @JoinColumn(name = "poll_id", nullable = false)
    private Poll poll;
}
