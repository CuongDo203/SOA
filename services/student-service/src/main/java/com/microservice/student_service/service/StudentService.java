package com.microservice.student_service.service;

import com.microservice.student_service.dto.request.StudentCreationRequest;
import com.microservice.student_service.entity.Student;

import java.util.List;

public interface StudentService {

    Student creatStudent(StudentCreationRequest studentCreationRequest);
    Student getStudentById(String id);
    Student getStudentByCode(String studentCode);
    List<Student> getAllStudents();
}
