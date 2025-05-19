package com.microservice.quiz_creation_service.clients;

import com.microservice.quiz_creation_service.dto.request.CreateQuestionRequest;
import com.microservice.quiz_creation_service.dto.response.ApiResponse;
import com.microservice.quiz_creation_service.dto.response.QuestionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "question-service", url = "http://localhost:9004/api/v1/questions")
public interface QuestionServiceClient {

    @PostMapping
    ApiResponse<QuestionResponse> createQuestion(CreateQuestionRequest request);
}