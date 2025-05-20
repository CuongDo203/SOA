package com.microservice.quiz_creation_service.clients;

import com.microservice.quiz_creation_service.dto.request.CreateQuestionRequest;
import com.microservice.quiz_creation_service.dto.response.ApiResponse;
import com.microservice.quiz_creation_service.dto.response.QuestionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "question-service")
public interface QuestionServiceClient {

    @PostMapping("/api/v1/questions")
    ApiResponse<QuestionResponse> createQuestion(CreateQuestionRequest request);
}