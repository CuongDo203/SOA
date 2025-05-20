package com.microservice.notification_service.clients;

import com.microservice.notification_service.dto.request.StudentIdsRequest;
import com.microservice.notification_service.dto.response.ApiResponse;
import com.microservice.notification_service.dto.response.StudentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "student-service")
public interface StudentServiceClient {

    @PostMapping("/api/v1/students/by-ids")
    ApiResponse<List<StudentResponse>> getStudentsByIds(@RequestBody StudentIdsRequest request);

}
