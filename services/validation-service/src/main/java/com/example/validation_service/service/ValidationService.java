package com.example.validation_service.service;
import com.example.validation_service.dto.*;
public interface ValidationService {
    ValidationResult validateQuestions(QuestionListRequest request);
    ValidationResult validateQuizConfig(QuizConfigRequest request);
    ValidationResult validateStudents(StudentListRequest request);
}

