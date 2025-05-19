package com.example.validation_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizConfigRequest {
//    private String id;
    private String quizName;
    private Integer durationMinutes;
    private Integer questionCount;
    private Double maxScore;
//    private String rules;
//    private String quizCode;
    private LocalDateTime start;
    private LocalDateTime end;

}
