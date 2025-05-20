package com.microservice.question_service.service;

import com.microservice.question_service.dto.request.CreateQuestionRequest;
import com.microservice.question_service.dto.response.QuestionResponse;

import java.util.List;

public interface QuestionService {
    QuestionResponse createQuestion(CreateQuestionRequest request);
}
