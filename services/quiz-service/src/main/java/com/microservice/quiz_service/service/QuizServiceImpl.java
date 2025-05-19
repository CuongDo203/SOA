package com.microservice.quiz_service.service;

import com.microservice.quiz_service.dto.request.QuizDTO;
import com.microservice.quiz_service.dto.response.CreateQuizResponse;
import com.microservice.quiz_service.entity.Quiz;
import com.microservice.quiz_service.exception.InvalidInputException;
import com.microservice.quiz_service.repository.QuizRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 8;
    private static final int MAX_CODE_GENERATION_ATTEMPTS = 10;

    private final QuizRepository quizRepository;

    @Override
    @Transactional
    public CreateQuizResponse createQuiz(QuizDTO quizDTO) {
        if(quizDTO.getTitle() == null || quizDTO.getTitle().trim().isEmpty()) {
            throw new InvalidInputException("Quiz title cannot be empty.");
        }
        if(quizDTO.getQuizConfigId() == null || quizDTO.getQuizConfigId().trim().isEmpty()) {
            throw new InvalidInputException("Quiz config ID cannot be empty.");
        }
        if(quizDTO.getQuestionIds() == null || quizDTO.getQuestionIds().isEmpty()) {
            throw new InvalidInputException("Question IDs cannot be empty.");
        }
        if(quizDTO.getAssignedStudentIds() == null || quizDTO.getAssignedStudentIds().isEmpty()) {
            throw new InvalidInputException("Assigned student IDs cannot be empty.");
        }
        String code = generateUniqueQuizCode();
        Quiz quiz = Quiz.builder()
                .title(quizDTO.getTitle())
                .quizConfigId(quizDTO.getQuizConfigId())
                .questionIds(quizDTO.getQuestionIds())
                .assignedStudentIds(quizDTO.getAssignedStudentIds())
                .code(code)
                .build();
        quiz = quizRepository.save(quiz);

        return mapEntityToResponse(quiz);
    }

    private CreateQuizResponse mapEntityToResponse(Quiz quiz) {
        if (quiz == null) return null;
        CreateQuizResponse dto = new CreateQuizResponse();
        dto.setId(quiz.getId());
        dto.setCode(quiz.getCode());
        dto.setTitle(quiz.getTitle());

        return dto;
    }

    private String generateUniqueQuizCode() {
        int attempts = 0;
        while (attempts < MAX_CODE_GENERATION_ATTEMPTS) {
            String generatedCode = "QZ-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

            // Kiểm tra tính duy nhất trong DB
            if (!quizRepository.existsByCode(generatedCode)) {
                return generatedCode; // Mã duy nhất được tìm thấy
            }

            System.out.println("Generated duplicate code: " + generatedCode + ". Retrying...");
            attempts++;
        }
        throw new RuntimeException("Failed to generate a unique quiz code after multiple attempts.");
    }

}
