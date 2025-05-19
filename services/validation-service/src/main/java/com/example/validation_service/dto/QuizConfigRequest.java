package com.example.validation_service.dto;

import java.sql.Date;
import java.time.LocalDateTime;

public class QuizConfigRequest {
    private String id;
    private String quizName;
    private Integer durationMinutes;
    // private Date startTime;
    // private Date finishTime;
    private Integer questionCount;
    private Double maxScore;
    private String rules;
    private String quizCode;
    private LocalDateTime start;
    private LocalDateTime end;

    public String getQuizName(){
        return this.quizName;
    }

    public Integer getDurationTime(){
        return this.durationMinutes;
    }

    public LocalDateTime getStartTime(){
        return this.start;
    }

    public LocalDateTime getFinishTime(){
        return this.end;
    }

    public Double getMaxScore(){
        return this.maxScore;
    }

    // public 
}
