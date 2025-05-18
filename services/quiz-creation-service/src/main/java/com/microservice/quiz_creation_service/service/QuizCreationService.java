package com.microservice.quiz_creation_service.service;

import com.microservice.quiz_creation_service.dto.response.CreateProcessResponse;
import com.microservice.quiz_creation_service.dto.response.ProcessStepResponse;
import org.springframework.web.multipart.MultipartFile;

public interface QuizCreationService {
    CreateProcessResponse startQuizCreationProcess();
    ProcessStepResponse importQuestions(String processId, MultipartFile file);
}
