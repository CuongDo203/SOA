package com.microservice.quiz_service.service;

import com.microservice.quiz_service.dto.request.QuizDTO;
import com.microservice.quiz_service.dto.response.CreateQuizResponse;

public interface QuizService {
    CreateQuizResponse createQuiz(QuizDTO quizDTO);
}
