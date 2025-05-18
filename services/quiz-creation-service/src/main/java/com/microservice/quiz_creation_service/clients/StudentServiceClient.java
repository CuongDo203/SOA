package com.microservice.quiz_creation_service.clients;

import com.microservice.quiz_creation_service.dto.request.StudentCreationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "student-service", url = "http://localhost:9001/api/v1/students")
public interface StudentServiceClient {
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    Object createStudent(@RequestBody StudentCreationRequest request);
}
