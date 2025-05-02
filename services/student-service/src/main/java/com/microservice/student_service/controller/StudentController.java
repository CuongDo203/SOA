package com.microservice.student_service.controller;

import com.microservice.student_service.dto.request.StudentCreationRequest;
import com.microservice.student_service.dto.response.ApiResponse;
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
        Student createdStudent = studentService.creatStudent(request);
        StudentResponse response = new StudentResponse();
        BeanUtils.copyProperties(createdStudent, response);
        return ApiResponse.<StudentResponse>builder()
                .message("Create student successfully!")
                .result(response)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<StudentResponse> getStudentById(@PathVariable String id) {
        log.info("Received request to get student by ID: {}", id);
        Student student = studentService.getStudentById(id);
        StudentResponse studentResponse = new StudentResponse();
        BeanUtils.copyProperties(student, studentResponse);
        return ApiResponse.<StudentResponse>builder()
                .message("Get student by id "+ id+" successfully!")
                .result(studentResponse)
                .build();
    }

    @GetMapping("/code/{studentCode}")
    public ApiResponse<StudentResponse> getStudentByCode(@PathVariable String studentCode) {
        Student student = studentService.getStudentByCode(studentCode);
        StudentResponse studentResponse = new StudentResponse();
        BeanUtils.copyProperties(student, studentResponse);
        return ApiResponse.<StudentResponse>builder()
                .message("Get student by code "+ studentCode+" successfully!")
                .result(studentResponse)
                .build();
    }

    @GetMapping
    public ApiResponse<List<StudentResponse>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        List<StudentResponse> studentResponses = new ArrayList<>();
        students.forEach(student -> {
            StudentResponse studentResponse = new StudentResponse();
            BeanUtils.copyProperties(student, studentResponse);
            studentResponses.add(studentResponse);
        });
        return ApiResponse.<List<StudentResponse>>builder()
                .message("Get all student successfully!")
                .result(studentResponses)
                .build();
    }
}
