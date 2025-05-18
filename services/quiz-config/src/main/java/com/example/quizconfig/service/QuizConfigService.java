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
                .quizName(dto.getQuizName())
                .durationMinutes(dto.getDurationMinutes())
                .questionCount(dto.getQuestionCount())
                .startTime(dto.getStartTime())
                .finishTime(dto.getFinishTime())
                .maxScore(dto.getMaxScore())
                .rules(dto.getRules())
                .build();
        config = repository.save(config);
        dto.setId(config.getId());
        return dto;
    }

    public Optional<QuizConfigDTO> getQuizConfig(String id) {
        return repository.findById(id)
                .map(config -> QuizConfigDTO.builder()
                        .id(config.getId())
                        .quizName(config.getQuizName())
                        .durationMinutes(config.getDurationMinutes())
                        .questionCount(config.getQuestionCount())
                        .startTime(config.getStartTime())
                        .finishTime(config.getFinishTime())
                        .maxScore(config.getMaxScore())
                        .rules(config.getRules())
                        .build());
    }
}