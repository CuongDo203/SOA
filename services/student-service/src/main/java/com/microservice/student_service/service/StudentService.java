package com.microservice.student_service.service;

import com.microservice.student_service.dto.request.StudentCheckRequest;
import com.microservice.student_service.dto.request.StudentCreationRequest;
import com.microservice.student_service.dto.response.StudentCheckResponse;
import com.microservice.student_service.dto.response.StudentResponse;
import com.microservice.student_service.entity.Student;

import java.util.List;
import java.util.Set;

public interface StudentService {
    StudentResponse creatStudent(StudentCreationRequest studentCreationRequest);
    StudentResponse getStudentById(String id);
    StudentResponse getStudentByCode(String studentCode);
    List<StudentResponse> getAllStudents();
    StudentCheckResponse checkStudentExistence(List<StudentCheckRequest> students);
    List<StudentResponse> getStudentsByIds(List<String> ids);
}
