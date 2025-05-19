package com.microservice.quiz_creation_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.event.dto.SendQuizCodeEvent;
import com.microservice.quiz_creation_service.dto.request.CreateQuizRequest;
import com.microservice.quiz_creation_service.dto.response.*;
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
import java.util.List;
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
    public ApiResponse<?> startQuizCreationProcess() {
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

    @PostMapping("/import-questions")
    public ApiResponse<?> importQuestionsFromExcel(@RequestPart("file") MultipartFile excelFile) {
        List<QuestionParsedResponse> response = quizCreationService.importQuestionsFromExcel(excelFile);
        return ApiResponse.builder()
                .message("Question imported successfully!")
                .result(response)
                .build();
    }

    @PostMapping("/import-students")
    public ApiResponse<?> importStudentsFromExcel(@RequestPart("file") MultipartFile excelFile) {
        List<StudentParsedResponse> response = quizCreationService.importStudentsFromExcel(excelFile);
        return ApiResponse.builder()
                .message("Student imported successfully!")
                .result(response)
                .build();
    }

    @PostMapping("/create-quiz")
    public ApiResponse<?> createQuiz(@RequestBody CreateQuizRequest createQuizRequest) {

        CreateQuizResponse response = quizCreationService.createQuiz(createQuizRequest);
        return ApiResponse.builder()
                .message("Quiz created successfully!")
                .result(response)
                .build();
    }
}
