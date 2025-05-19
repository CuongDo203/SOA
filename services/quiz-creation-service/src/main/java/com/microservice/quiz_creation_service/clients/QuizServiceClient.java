package com.microservice.quiz_creation_service.clients;

import com.microservice.quiz_creation_service.dto.request.QuizDTO;
import com.microservice.quiz_creation_service.dto.response.CreateQuizResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "quiz-service", url = "http://localhost:9005/api/v1/quiz")
public interface QuizServiceClient {

    @PostMapping
    public CreateQuizResponse createQuiz(@RequestBody QuizDTO quizDTO);

}
