package com.microservice.student_service.controller;

import com.microservice.student_service.dto.request.StudentCheckRequest;
import com.microservice.student_service.dto.request.StudentCreationRequest;
import com.microservice.student_service.dto.response.ApiResponse;
import com.microservice.student_service.dto.response.StudentCheckResponse;
import com.microservice.student_service.dto.response.StudentResponse;
import com.microservice.student_service.entity.Student;
import com.microservice.student_service.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/students")
@RequiredArgsConstructor
@Slf4j
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public ApiResponse<StudentResponse> createStudent(@Valid @RequestBody StudentCreationRequest request) {
        log.info("Received request to create student: {}", request.getStudentCode());
        StudentResponse createdStudent = studentService.creatStudent(request);
        return ApiResponse.<StudentResponse>builder()
                .message("Create student successfully!")
                .result(createdStudent)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<StudentResponse> getStudentById(@PathVariable String id) {
        log.info("Received request to get student by ID: {}", id);
        StudentResponse student = studentService.getStudentById(id);
        return ApiResponse.<StudentResponse>builder()
                .message("Get student by id "+ id+" successfully!")
                .result(student)
                .build();
    }

    @GetMapping("/code/{studentCode}")
    public ApiResponse<StudentResponse> getStudentByCode(@PathVariable String studentCode) {
        StudentResponse student = studentService.getStudentByCode(studentCode);
        return ApiResponse.<StudentResponse>builder()
                .message("Get student by code "+ studentCode+" successfully!")
                .result(student)
                .build();
    }

    @GetMapping
    public ApiResponse<List<StudentResponse>> getAllStudents() {
        List<StudentResponse> students = studentService.getAllStudents();
        return ApiResponse.<List<StudentResponse>>builder()
                .message("Get all student successfully!")
                .result(students)
                .build();
    }

    @PostMapping("/check-existence")
    public ApiResponse<StudentCheckResponse> checkStudentExistence(@Valid @RequestBody StudentCheckRequest request) {
        StudentCheckResponse studentCheckResponse = studentService.checkStudentExistence(request.getStudentCodes());
        return ApiResponse.<StudentCheckResponse>builder()
                .message("Student existence check completed!")
                .result(studentCheckResponse)
                .build();
    }
}
