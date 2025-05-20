package com.microservice.quiz_creation_service.clients;

import com.microservice.quiz_creation_service.dto.request.ListStudentCheckRequest;
import com.microservice.quiz_creation_service.dto.request.StudentCreationRequest;
import com.microservice.quiz_creation_service.dto.response.ApiResponse;
import com.microservice.quiz_creation_service.dto.response.StudentCheckResponse;
import com.microservice.quiz_creation_service.dto.response.StudentResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "student-service")
public interface StudentServiceClient {
    @PostMapping(value = "/api/v1/students", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<StudentResponse> createStudent(@RequestBody StudentCreationRequest request);

    @PostMapping("/api/v1/students/check-existence")
    ApiResponse<StudentCheckResponse> checkStudentExistence(@Valid @RequestBody ListStudentCheckRequest request);
}
