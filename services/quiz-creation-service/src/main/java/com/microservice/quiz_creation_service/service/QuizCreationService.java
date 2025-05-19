package com.microservice.quiz_creation_service.service;

import com.microservice.quiz_creation_service.dto.request.CreateQuizRequest;
import com.microservice.quiz_creation_service.dto.request.QuizConfigDTO;
import com.microservice.quiz_creation_service.dto.response.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface QuizCreationService {
    List<QuestionParsedResponse> importAndValidateQuestions(MultipartFile excelFile);
    List<StudentParsedResponse> importAndValidateStudents(MultipartFile excelFile);
    QuizParsedResponse importAndValidateQuizConfig(QuizConfigDTO quizConfigDTO);
    CreateQuizResponse createQuiz(CreateQuizRequest createQuizRequest);
}
