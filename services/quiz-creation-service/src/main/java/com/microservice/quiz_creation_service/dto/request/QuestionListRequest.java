package com.microservice.quiz_creation_service.dto.request;

import com.microservice.quiz_creation_service.dto.response.QuestionParsedResponse;
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
    private List<QuestionParsedResponse> questions;

    public List<QuestionParsedResponse> getQuestions() {
        return this.questions;
    }
}

