package com.example.quizconfig.controller;

import com.example.quizconfig.dto.QuizConfigDTO;
import com.example.quizconfig.service.QuizConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/quiz-config")
public class QuizConfigController {
    @Autowired
    private QuizConfigService service;

    @PostMapping
    public ResponseEntity<QuizConfigDTO> create(@RequestBody QuizConfigDTO dto) {
        return ResponseEntity.ok(service.createQuizConfig(dto));
    }

}