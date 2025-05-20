package com.microservice.student_service.controller;

import com.microservice.student_service.dto.request.ListStudentCheckRequest;
import com.microservice.student_service.dto.request.StudentCheckRequest;
import com.microservice.student_service.dto.request.StudentCreationRequest;
import com.microservice.student_service.dto.request.StudentIdsRequest;
import com.microservice.student_service.dto.response.ApiResponse;
import com.microservice.student_service.dto.response.StudentCheckResponse;
import com.microservice.student_service.dto.response.StudentResponse;
import com.microservice.student_service.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/students")
@RequiredArgsConstructor
@Slf4j
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public ApiResponse<StudentResponse> createStudent(@Valid @RequestBody StudentCreationRequest request) {
        log.info("Received request to create student: {}", request.getStudentCode());
        StudentResponse createdStudent = studentService.createStudent(request);
        return ApiResponse.<StudentResponse>builder()
                .message("Create student successfully!")
                .result(createdStudent)
                .build();
    }

    @PostMapping("/check-existence")
    public ApiResponse<StudentCheckResponse> checkStudentExistence(@Valid @RequestBody ListStudentCheckRequest request) {
        List<StudentCheckRequest> studentCheckRequests = request.getStudentChecks();
        StudentCheckResponse studentCheckResponse = studentService.checkStudentExistence(studentCheckRequests);
        return ApiResponse.<StudentCheckResponse>builder()
                .message("Student existence check completed!")
                .result(studentCheckResponse)
                .build();
    }

    @PostMapping("/by-ids")
    public ApiResponse<List<StudentResponse>> getStudentsByIds(@RequestBody StudentIdsRequest request) {
        log.info("Received request to get students by IDs: {}", request.getIds());
        log.info("IDs: {}", request.getIds());
        List<StudentResponse> students = studentService.getStudentsByIds(request.getIds());

        return ApiResponse.<List<StudentResponse>>builder()
                .message("Get students by IDs successfully!")
                .result(students)
                .build();
    }
}
