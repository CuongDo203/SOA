package com.example.validation_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionListRequest {
    private List<QuestionDto> questions; 

    public List<QuestionDto> getQuestions() {
        return this.questions;
    }
}

