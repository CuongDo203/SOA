package com.example.quizconfig.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizConfigDTO {
    private String id;
    private String quizName;
    private Integer durationMinutes;
    private Integer questionCount;
    private Double maxScore;
    private String rules;
    private String quizCode;
    private LocalDateTime start;
    private LocalDateTime end;
}
