package com.harbourspace.cs308.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepositoryDto {
    @NotBlank(message = "Repository name is required")
    private String name;

    @NotBlank(message = "Repository owner is required")
    private String owner;
}
