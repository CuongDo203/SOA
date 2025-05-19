package com.microservice.quiz_creation_service.clients;

import com.microservice.quiz_creation_service.configuration.FeignMultipartConfig;
import com.microservice.quiz_creation_service.dto.response.QuestionParsedResponse;
import com.microservice.quiz_creation_service.dto.response.StudentParsedResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = "import-service",
        url = "http://localhost:8082/api/v1/import",
        configuration = FeignMultipartConfig.class)
public interface ImportServiceClient {
    @PostMapping(value = "/questions/excel", consumes = "multipart/form-data")
    List<QuestionParsedResponse> importQuestionsFromExcel(@RequestPart(value = "file") MultipartFile excelFile);

    @PostMapping(value = "/students/excel", consumes = "multipart/form-data")
    List<StudentParsedResponse> importStudentsFromExcel(@RequestPart(value = "file") MultipartFile excelFile);
}
