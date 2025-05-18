package com.microservice.quiz_creation_service.clients;

import com.microservice.quiz_creation_service.dto.response.QuestionParsedResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = "import-service")
public interface ImportServiceClient {
    @PostMapping(value = "/questions")
    List<QuestionParsedResponse> importQuestions(MultipartFile file);
}
