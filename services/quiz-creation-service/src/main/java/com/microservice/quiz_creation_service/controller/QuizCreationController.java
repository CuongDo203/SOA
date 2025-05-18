package com.microservice.quiz_creation_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.event.dto.SendQuizCodeEvent;
import com.microservice.quiz_creation_service.dto.request.CreateQuizRequest;
import com.microservice.quiz_creation_service.dto.response.ApiResponse;
import com.microservice.quiz_creation_service.dto.response.CreateProcessResponse;
import com.microservice.quiz_creation_service.dto.response.CreateQuizResponse;
import com.microservice.quiz_creation_service.dto.response.ProcessStepResponse;
import com.microservice.quiz_creation_service.service.QuizCreationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/quiz-creation")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class QuizCreationController {

    QuizCreationService quizCreationService;
    KafkaTemplate<String, Object> kafkaTemplate;

    @PostMapping("/start")
    public ApiResponse startQuizCreationProcess() {
        CreateProcessResponse response = quizCreationService.startQuizCreationProcess();
        return ApiResponse.builder()
                .message(response.getMessage())
                .result(response)
                .build();
    }


//    @PostMapping(value = "/{processId}/import/questions", consumes = {"multipart/form-data"})
//    public ApiResponse importQuestions(@PathVariable String processId,
//                                       @RequestParam("questionFile") MultipartFile questionFile) {
//        ProcessStepResponse response = quizCreationService.importQuestions(processId, questionFile);
//        if("FAILED".equals(response.getStatus())) {
//
//        }
//        return ApiResponse.builder()
//                .message(response.getMessage())
//                .result(response)
//                .build();
//    }


    @PostMapping("/create-quiz")
    public ApiResponse createQuiz(@RequestBody CreateQuizRequest createQuizRequest) {

        CreateQuizResponse response = quizCreationService.createQuiz(createQuizRequest);
        return ApiResponse.builder()
                .message("Quiz created successfully!")
                .result(response)
                .build();
    }
}
