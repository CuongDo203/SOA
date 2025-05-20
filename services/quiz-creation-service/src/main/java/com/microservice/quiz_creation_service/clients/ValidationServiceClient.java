package com.microservice.quiz_creation_service.clients;

import com.microservice.quiz_creation_service.dto.request.QuestionListRequest;
import com.microservice.quiz_creation_service.dto.request.QuizConfigDTO;
import com.microservice.quiz_creation_service.dto.request.StudentListRequest;
import com.microservice.quiz_creation_service.dto.response.ValidationResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "validation-service")
public interface ValidationServiceClient {
    @PostMapping("/api/v1/validation/questions")
    ValidationResult validateQuestions(@RequestBody QuestionListRequest request);

    @PostMapping("/api/v1/validation/quiz-config")
    ValidationResult validateQuizConfig(@RequestBody QuizConfigDTO request);

    @PostMapping("/api/v1/validation/students")
    ValidationResult validateStudents(@RequestBody StudentListRequest request);
}
