package com.microservice.question_service.controller;

import com.microservice.question_service.dto.request.CreateQuestionRequest;
import com.microservice.question_service.dto.response.ApiResponse;
import com.microservice.question_service.dto.response.QuestionResponse;
import com.microservice.question_service.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping
    public ApiResponse<QuestionResponse> createQuestion(@Valid @RequestBody CreateQuestionRequest request) {
        QuestionResponse questionResponse = questionService.createQuestion(request);
        return ApiResponse.<QuestionResponse>builder()
                .message("Create question successfully!")
                .result(questionResponse)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<?> getQuestionById(@PathVariable String id) {
        QuestionResponse questionResponse = questionService.getQuestionById(id);
        return ApiResponse.<QuestionResponse>builder()
                .message("Get question successfully!")
                .result(questionResponse)
                .build();
    }

    @PostMapping("/by-ids")
    public ApiResponse<?> getQuestionsByIds(@RequestBody List<String> ids) {
        List<QuestionResponse> responses = questionService.getQuestionsByIds(ids);
        return ApiResponse.builder()
                .message("Get questions by ids successfully!")
                .result(responses)
                .build();
    }

    @GetMapping
    public ApiResponse<List<QuestionResponse>> getAllQuestions() {
        List<QuestionResponse> questions = questionService.getAllQuestions();
        return ApiResponse.<List<QuestionResponse>>builder()
                .message("Get all questions successfully!")
                .result(questions)
                .build();
    }
}
