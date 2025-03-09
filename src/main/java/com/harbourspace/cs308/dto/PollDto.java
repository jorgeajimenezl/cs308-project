package com.harbourspace.cs308.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PollDto {
    @NotBlank(message = "Title is required")
    String question;
    
    Boolean multipleChoice;
    Boolean isPublic;
    List<String> options;
}
