package com.example.quizconfig.service;

import com.example.quizconfig.dto.QuizConfigDTO;
import com.example.quizconfig.entity.QuizConfig;
import com.example.quizconfig.repository.QuizConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QuizConfigService {
    @Autowired
    private QuizConfigRepository repository;

    public QuizConfigDTO createQuizConfig(QuizConfigDTO dto) {
        QuizConfig config = QuizConfig.builder()
                .durationMinutes(dto.getDurationMinutes())
                .questionCount(dto.getQuestionCount())
                .start(dto.getStart())
                .end(dto.getEnd())
                .maxScore(dto.getMaxScore())
                .rules(dto.getRules())
                .build();
        config = repository.save(config);
        dto.setId(config.getId());
        return dto;
    }

}