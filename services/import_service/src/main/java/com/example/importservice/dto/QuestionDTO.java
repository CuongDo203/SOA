package com.example.importservice.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {
    private Long id;

    @NotBlank(message = "Question content cannot be blank")
    private String content;

    @NotEmpty(message = "Options cannot be empty")
    @Size(min = 2, message = "At least two options are required")
    private List<String> options;

    @NotNull(message = "Correct answer index cannot be null")
    private Integer correctAnswerIndex;

    private String subject;
    private String difficulty;
}