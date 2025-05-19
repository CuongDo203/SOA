package com.example.validation_service.dto;

import java.util.List;

public class QuestionListRequest {
    private List<QuestionDto> questions; 
    
    
    public List<QuestionDto> getQuestions() {
        return this.questions;
    }
}

