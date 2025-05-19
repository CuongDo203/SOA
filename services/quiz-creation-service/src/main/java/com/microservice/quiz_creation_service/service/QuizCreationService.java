package com.microservice.quiz_creation_service.service;

import com.microservice.quiz_creation_service.dto.request.CreateQuizRequest;
import com.microservice.quiz_creation_service.dto.response.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface QuizCreationService {
    CreateProcessResponse startQuizCreationProcess();
//    ProcessStepResponse importQuestions(String processId, MultipartFile file);
    List<QuestionParsedResponse> importQuestionsFromExcel(MultipartFile excelFile);
    List<StudentParsedResponse> importStudentsFromExcel(MultipartFile excelFile);
    CreateQuizResponse createQuiz(CreateQuizRequest createQuizRequest);
}
