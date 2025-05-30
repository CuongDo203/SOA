package com.microservice.quiz_creation_service.service;

import com.microservice.event.dto.SendQuizCodeEvent;
import com.microservice.quiz_creation_service.clients.*;
import com.microservice.quiz_creation_service.dto.request.*;
import com.microservice.quiz_creation_service.dto.response.*;
import com.microservice.quiz_creation_service.exception.ValidationException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class QuizCreationServiceImpl implements QuizCreationService{

    ImportServiceClient importServiceClient;
    StudentServiceClient studentServiceClient;
    QuizConfigServiceClient quizConfigServiceClient;
    QuestionServiceClient questionServiceClient;
    QuizServiceClient quizServiceClient;
    ValidationServiceClient validationServiceClient;

    KafkaTemplate<String, SendQuizCodeEvent> kafkaTemplate;

    @Override
    public List<QuestionParsedResponse> importAndValidateQuestions(MultipartFile excelFile) {
            List<QuestionParsedResponse> importedQuestions = importServiceClient.importQuestionsFromExcel(excelFile);
            QuestionListRequest validationRequest = QuestionListRequest.builder()
                    .questions(importedQuestions)
                    .build();
            ValidationResult validationResult = validationServiceClient.validateQuestions(validationRequest);
            if (!validationResult.isValid()) {
                log.error("Validation failed for imported questions: {}", validationResult.getErrors());
                throw new ValidationException("Validation failed for imported questions",
                        validationResult.getErrors());
            }
            return importedQuestions;
    }

    @Override
    public List<StudentParsedResponse> importAndValidateStudents(MultipartFile excelFile) {
        try {
            List<StudentParsedResponse> importedStudents = importServiceClient.importStudentsFromExcel(excelFile);
            log.info("Imported students: {}", importedStudents);
            StudentListRequest validationRequest = new StudentListRequest();
            validationRequest.setStudents(importedStudents);

            ValidationResult validationResult = validationServiceClient.validateStudents(validationRequest);

            if (!validationResult.isValid()) {
                log.error("Validation failed for imported students: {}", validationResult.getErrors());
                throw new ValidationException("Validation failed for imported students", validationResult.getErrors());
            }

            return importedStudents;

        } catch (Exception e) {
            log.error("Error when importing students: ", e);
            throw new RuntimeException("Failed to import students from Excel", e);
        }
    }

    @Override
    public QuizParsedResponse importAndValidateQuizConfig(QuizConfigDTO quizConfigDTO) {
            // Validate the quiz configuration
            ValidationResult validationResult = validationServiceClient.validateQuizConfig(quizConfigDTO);

            // Check if validation was successful
            if (!validationResult.isValid()) {
                log.error("Validation failed for quiz configuration: {}", validationResult.getErrors());
                throw new ValidationException("Validation failed for quiz configuration", validationResult.getErrors());
            }

            // Create and return a QuizParsedResponse with the validated quiz configuration
            QuizParsedResponse response = QuizParsedResponse.builder()
                    .durationMinutes(quizConfigDTO.getDurationMinutes())
                    .questionCount(quizConfigDTO.getQuestionCount())
                    .maxScore(quizConfigDTO.getMaxScore())
                    .rules(quizConfigDTO.getRules())
                    .start(quizConfigDTO.getStart())
                    .end(quizConfigDTO.getEnd())
                    .build();

            return response;
    }


    @Override
    public CreateQuizResponse createQuiz(CreateQuizRequest createQuizRequest) {
        // Validation checks
        List<StudentCreationRequest> students = createQuizRequest.getStudents();
        if(students == null || students.isEmpty()) {
            throw new ValidationException("Students list cannot be empty", new ArrayList<>());
        }

        QuizConfigDTO quizConfig = createQuizRequest.getQuizConfig();
        if(quizConfig == null) {
            throw new ValidationException("Quiz configuration cannot be empty", new ArrayList<>());
        }

        List<CreateQuestionRequest> questionRequests = createQuizRequest.getQuestions();
        if(questionRequests == null || questionRequests.isEmpty()) {
            throw new ValidationException("Questions list cannot be empty", new ArrayList<>());
        }

        // Extract student codes for checking
        List<String> studentCodes = students.stream()
                .map(StudentCreationRequest::getStudentCode)
                .collect(Collectors.toList());

        // Create a request to check student existence
        List<StudentCheckRequest> studentChecks = students.stream()
                .map(student -> StudentCheckRequest.builder()
                        .studentCode(student.getStudentCode())
                        .build())
                .collect(Collectors.toList());

        // Check if all students exist in the system
        ApiResponse<StudentCheckResponse> checkResponse = studentServiceClient.checkStudentExistence(ListStudentCheckRequest
                .builder()
                .studentChecks(studentChecks)
                .build());
        StudentCheckResponse checkResult = checkResponse.getResult();

        // If there are any non-existing student codes, throw an exception
        if (checkResult.getNonExistingCodes() != null && !checkResult.getNonExistingCodes().isEmpty()) {
            throw new ValidationException("The following student codes do not exist in the system: "
                    + String.join(", ", checkResult.getNonExistingCodes()), new ArrayList<>());
        }

        // All students exist, continue with quiz creation
        CreateQuizResponse response = new CreateQuizResponse();

        // Get the student IDs from the map in the response
        Map<String, String> studentCodeToIdMap = checkResult.getStudentCodeToIdMap();
        List<String> studentIds = studentCodes.stream()
                .map(studentCodeToIdMap::get)
                .collect(Collectors.toList());

        response.setStudentIds(studentIds);

        QuizConfigDTO savedQuizConfig = quizConfigServiceClient.createQuizConfig(quizConfig);
        if(savedQuizConfig != null) {
            log.info("Quiz config created: {}", savedQuizConfig);
            response.setQuizConfigId(savedQuizConfig.getId());
        }

        // Save the questions
        List<String> savedQuestionIds = new ArrayList<>();
        for(CreateQuestionRequest questionRequest : questionRequests) {
            ApiResponse<QuestionResponse> questionResponse = questionServiceClient.createQuestion(questionRequest);
            log.info("Question created: {}", questionResponse);
            QuestionResponse questionData = questionResponse.getResult();
            savedQuestionIds.add(questionData.getId());
        }
        response.setQuestionIds(savedQuestionIds);

        // Create the quiz with existing student IDs
        QuizDTO quizDto = QuizDTO.builder()
                .title(createQuizRequest.getQuizTitle())
                .quizConfigId(savedQuizConfig.getId())
                .questionIds(savedQuestionIds)
                .assignedStudentIds(studentIds)
                .build();

        response = quizServiceClient.createQuiz(quizDto);

        // Send notification
        SendQuizCodeEvent event = new SendQuizCodeEvent();
        event.setQuizCode(response.getCode());
        event.setStudentIds(studentIds);
        kafkaTemplate.send("quiz-creation-notification-topic", event);
        log.info("Published quiz creation event for quiz code: {}", response.getCode());

        return response;
    }
}
