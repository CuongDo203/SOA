package com.microservice.quiz_creation_service.controller;

import com.microservice.quiz_creation_service.dto.request.CreateQuizRequest;
import com.microservice.quiz_creation_service.dto.request.QuizConfigDTO;
import com.microservice.quiz_creation_service.dto.response.*;
import com.microservice.quiz_creation_service.exception.ValidationException;
import com.microservice.quiz_creation_service.service.QuizCreationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/quiz-creation")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class QuizCreationController {

    QuizCreationService quizCreationService;
    KafkaTemplate<String, Object> kafkaTemplate;

    @PostMapping("/verify/questions")
    public ApiResponse<?> verifyQuestionData(@RequestPart("file") MultipartFile excelFile) {
            List<QuestionParsedResponse> response = quizCreationService.importAndValidateQuestions(excelFile);
            return ApiResponse.builder()
                    .message("Questions imported and validated successfully!")
                    .result(response)
                    .build();

    }

    @PostMapping("/verify/students")
    public ApiResponse<?> verifyStudentData(@RequestPart("file") MultipartFile excelFile) {
            List<StudentParsedResponse> response = quizCreationService.importAndValidateStudents(excelFile);
            return ApiResponse.builder()
                    .message("Students imported and validated successfully!")
                    .result(response)
                    .build();
    }

    @PostMapping("/verify/quiz-config")
    public ApiResponse<?> verifyQuizConfig(@RequestBody QuizConfigDTO quizConfigRequest) {
            QuizParsedResponse response = quizCreationService.importAndValidateQuizConfig(quizConfigRequest);
            return ApiResponse.builder()
                    .message("Quiz configuration validated successfully!")
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
