package com.microservice.quiz_creation_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.quiz_creation_service.clients.ImportServiceClient;
import com.microservice.quiz_creation_service.clients.QuestionServiceClient;
import com.microservice.quiz_creation_service.clients.QuizConfigServiceClient;
import com.microservice.quiz_creation_service.clients.StudentServiceClient;
import com.microservice.quiz_creation_service.dto.request.CreateQuestionRequest;
import com.microservice.quiz_creation_service.dto.request.CreateQuizRequest;
import com.microservice.quiz_creation_service.dto.request.QuizConfigDTO;
import com.microservice.quiz_creation_service.dto.request.StudentCreationRequest;
import com.microservice.quiz_creation_service.dto.response.*;
import com.microservice.quiz_creation_service.entity.QuizCreationProcess;
import com.microservice.quiz_creation_service.repository.QuizCreationProcessRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class QuizCreationServiceImpl implements QuizCreationService{

    QuizCreationProcessRepository processRepository;
    ObjectMapper objectMapper;
    ImportServiceClient importServiceClient;
    StudentServiceClient studentServiceClient;
    QuizConfigServiceClient quizConfigServiceClient;
    QuestionServiceClient questionServiceClient;

    KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    public CreateProcessResponse startQuizCreationProcess() {
        QuizCreationProcess process = new QuizCreationProcess();
        process.setStatus(QuizCreationProcess.Status.PENDING_QUESTIONS);
        QuizCreationProcess savedProcess = processRepository.save(process);
        log.info("QuizCreationService: Start with id: {}", savedProcess.getProcessId());
        CreateProcessResponse response = CreateProcessResponse.builder()
                .processId(savedProcess.getProcessId())
                .status(savedProcess.getStatus().name())
                .message("Quiz creation process started. Please import questions.")
                .build();
        return response;
    }

    public CreateQuizResponse createQuiz(CreateQuizRequest createQuizRequest) {
        List<StudentCreationRequest> students = createQuizRequest.getStudents();
        CreateQuizResponse response = new CreateQuizResponse();
        if(students == null || students.isEmpty()) {
            return null;
        }
        QuizConfigDTO quizConfig = createQuizRequest.getQuizConfig();
        if(quizConfig == null) {
            return null;
        }
        List<CreateQuestionRequest> questionRequests = createQuizRequest.getQuestions();
        if(questionRequests == null || questionRequests.isEmpty()) {
            return null;
        }
        List<String> savedStudentIds = new ArrayList<>();
        for(StudentCreationRequest student : students) {
            ApiResponse<StudentResponse> studentResponse = studentServiceClient.createStudent(student);
            log.info("Student created: {}", studentResponse);
            StudentResponse studentData = studentResponse.getResult();
            savedStudentIds.add(studentData.getId());
        }
        response.setStudentIds(savedStudentIds);
        String quizCode = "QZ-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        log.info("Quiz code: {}", quizCode);
        QuizConfigDTO quizConfigDTO = createQuizRequest.getQuizConfig();
        QuizConfigDTO savedQuizConfig = quizConfigServiceClient.createQuizConfig(quizConfigDTO);
        if(savedQuizConfig != null) {
            log.info("Quiz config created: {}", savedQuizConfig);
            response.setQuizConfigId(savedQuizConfig.getId());
        }
        List<String> savedQuestionIds = new ArrayList<>();
        for(CreateQuestionRequest questionRequest : questionRequests) {
            ApiResponse<QuestionResponse> questionResponse = questionServiceClient.createQuestion(questionRequest);
            log.info("Question created: {}", questionResponse);
            QuestionResponse questionData = questionResponse.getResult();
            savedQuestionIds.add(questionData.getId());
        }
        response.setQuestionIds(savedQuestionIds);
        return response;
    }
}
