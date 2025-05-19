package com.example.validation_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizConfigDTO {
    private String id;
    private String quizName;
    private Integer durationMinutes;
    private Integer questionCount;
    private Double maxScore;
    private LocalDateTime start;
    private LocalDateTime end;

}
