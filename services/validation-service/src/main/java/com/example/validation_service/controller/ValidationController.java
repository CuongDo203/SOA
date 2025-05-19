package com.example.validation_service.controller;

import com.example.validation_service.dto.QuestionListRequest;
import com.example.validation_service.dto.QuizConfigDTO;
import com.example.validation_service.dto.StudentListRequest;
import com.example.validation_service.service.ValidationService;
import com.example.validation_service.dto.ValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/validation")
public class ValidationController {

    @Autowired
    private ValidationService validationService;

    @PostMapping("/questions")
    public ResponseEntity<ValidationResult> validateQuestions(@RequestBody QuestionListRequest request) {
        return ResponseEntity.ok(validationService.validateQuestions(request));
    }

    @PostMapping("/quiz-config")
    public ResponseEntity<ValidationResult> validateQuizConfig(@RequestBody QuizConfigDTO request) {
        return ResponseEntity.ok(validationService.validateQuizConfig(request));
    }

    @PostMapping("/students")
    public ResponseEntity<ValidationResult> validateStudents(@RequestBody StudentListRequest request) {
        return ResponseEntity.ok(validationService.validateStudents(request));
    }
}