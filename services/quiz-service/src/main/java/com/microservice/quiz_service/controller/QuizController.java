package com.microservice.quiz_service.controller;

import com.microservice.quiz_service.dto.request.QuizDTO;
import com.microservice.quiz_service.dto.response.CreateQuizResponse;
import com.microservice.quiz_service.service.QuizService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/quiz")
@RequiredArgsConstructor
@Slf4j
public class QuizController {

    private final QuizService quizService;

    @PostMapping
    public ResponseEntity<?> createQuiz(@RequestBody QuizDTO quizDTO) {
        try {
            CreateQuizResponse response = quizService.createQuiz(quizDTO);
            return ResponseEntity.ok(response);
        }
        catch (Exception e) {
            log.error("Failed to create quiz: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lỗi khi tạo quiz " + e.getMessage());
        }
    }

}
