package com.example.importservice.client;

import com.example.importservice.dto.QuestionDTO;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "question-service", url = "${question.service.url}")
public interface QuestionServiceClient {

    @PostMapping("/questions") // Endpoint của QuestionController trong question-service
    ResponseEntity<QuestionDTO> createQuestion(@Valid @RequestBody QuestionDTO questionDTO);
}