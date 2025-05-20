package com.microservice.student_service.service;

import com.microservice.student_service.dto.request.StudentCheckRequest;
import com.microservice.student_service.dto.request.StudentCreationRequest;
import com.microservice.student_service.dto.response.StudentCheckResponse;
import com.microservice.student_service.dto.response.StudentResponse;
import com.microservice.student_service.entity.Student;
import com.microservice.student_service.exception.DataConflictException;
import com.microservice.student_service.exception.ErrorCode;
import com.microservice.student_service.exception.ResourceNotFoundException;
import com.microservice.student_service.repository.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class StudentServiceImpl implements StudentService{

    private final StudentRepository studentRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    @Override
    public StudentResponse createStudent(StudentCreationRequest studentCreationRequest) {
        if (studentRepository.existsByStudentCode(studentCreationRequest.getStudentCode())) {
            log.warn("Student code {} already exist", studentCreationRequest.getStudentCode());
            throw new DataConflictException(ErrorCode.STUDENT_EXISTED);
        }
        if(studentCreationRequest.getEmail() != null
                && studentRepository.existsByEmail(studentCreationRequest.getEmail())) {
            log.warn("Student email {} already exists.", studentCreationRequest.getEmail());
            throw new DataConflictException(ErrorCode.STUDENT_EXISTED);
        }
        Student student = Student.fromStudentCreationRequest(studentCreationRequest);
        Student savedStudent = studentRepository.save(student);
        log.info("Successfully created student with ID: {} ans code {}", student.getId(), student.getStudentCode());
        StudentResponse response = new StudentResponse();
        BeanUtils.copyProperties(savedStudent, response);
        return response;
    }

    @Override
    public StudentCheckResponse checkStudentExistence(List<StudentCheckRequest> students) {
        if(students == null || students.isEmpty()) {
            return StudentCheckResponse.builder()
                    .existingCodes(Set.of())
                    .nonExistingCodes(Set.of())
                    .studentCodeToIdMap(Map.of())
                    .build();
        }

        Set<String> studentCodes = students.stream()
                .map(StudentCheckRequest::getStudentCode)
                .collect(Collectors.toSet());

        List<Student> existingStudents = studentRepository.findByStudentCodes(studentCodes);

        Set<String> existingCodes = existingStudents.stream()
                .map(Student::getStudentCode)
                .collect(Collectors.toSet());

        // Create a map of student codes to their IDs
        Map<String, String> studentCodeToIdMap = existingStudents.stream()
                .collect(Collectors.toMap(
                        Student::getStudentCode,
                        Student::getId
                ));

        Set<String> nonExistingCodes = new HashSet<>(studentCodes);
        nonExistingCodes.removeAll(existingCodes);

        return StudentCheckResponse.builder()
                .existingCodes(existingCodes)
                .nonExistingCodes(nonExistingCodes)
                .studentCodeToIdMap(studentCodeToIdMap)
                .build();
    }

    @Override
    public List<StudentResponse> getStudentsByIds(List<String> ids) {
        log.info("Getting students with IDs: {}", ids);

        if (ids == null || ids.isEmpty()) {
            log.warn("Empty or null list of IDs provided");
            return Collections.emptyList();
        }

        List<Student> students = studentRepository.findAllById(ids);

        log.info("Found {} students out of {} requested IDs", students.size(), ids.size());

        return students.stream()
                .map(student -> StudentResponse.builder()
                        .id(student.getId())
                        .studentCode(student.getStudentCode())
                        .firstName(student.getFirstName())
                        .lastName(student.getLastName())
                        .email(student.getEmail())
                        .className(student.getClassName())
                        .build())
                .collect(Collectors.toList());
    }
}
