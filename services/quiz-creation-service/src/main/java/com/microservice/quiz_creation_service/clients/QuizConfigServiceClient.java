package com.microservice.quiz_creation_service.clients;

import com.microservice.quiz_creation_service.dto.request.QuizConfigDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "quiz-config", url = "http://localhost:8081/api/v1/quiz-config")
public interface QuizConfigServiceClient {
    @PostMapping
    QuizConfigDTO createQuizConfig(QuizConfigDTO quizConfigDTO);
}
