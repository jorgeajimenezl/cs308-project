package com.harbourspace.cs308.dto;

import com.harbourspace.cs308.model.RepositoryActivityType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepositoryActivityDto {
    private String repo;
    private RepositoryActivityType type;
    private String description;
    private String timestamp;   
}
