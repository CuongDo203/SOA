package com.microservice.question_service.service;

import com.microservice.question_service.dto.request.CreateQuestionRequest;
import com.microservice.question_service.dto.response.QuestionResponse;
import com.microservice.question_service.entity.Question;
import com.microservice.question_service.exception.ErrorCode;
import com.microservice.question_service.exception.ResourceNotFoundException;
import com.microservice.question_service.repository.QuestionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService{

    private final QuestionRepository questionRepository;

    @Override
    @Transactional
    public QuestionResponse createQuestion(CreateQuestionRequest request) {
        Question question = Question.builder()
                .content( request.getContent())
                .optionA( request.getOptionA())
                .optionB( request.getOptionB())
                .optionC( request.getOptionC())
                .optionD( request.getOptionD())
                .answerKey( request.getAnswerKey())
                .build();
        Question savedQuestion = questionRepository.save(question);
        return mapToQuestionResponse(savedQuestion);
    }
    
    private QuestionResponse mapToQuestionResponse(Question question) {
        return QuestionResponse.builder()
                .id(question.getId())
                .content(question.getContent())
                .optionA(question.getOptionA())
                .optionB(question.getOptionB())
                .optionC(question.getOptionC())
                .optionD(question.getOptionD())
                .answerKey(question.getAnswerKey())
                .build();
    }

}
