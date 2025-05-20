package com.example.quizconfig.dto;

// import java.sql.Date;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizConfigDTO {
    private String id;
    private Integer durationMinutes;
    private Integer questionCount;
    private Double maxScore;
    private String rules;
    private LocalDateTime start;
    private LocalDateTime end;
}
