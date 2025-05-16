package com.microservice.student_service.service;

import com.microservice.event.dto.NotificationEvent;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class StudentServiceImpl implements StudentService{

    private final StudentRepository studentRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    @Override
    public StudentResponse creatStudent(StudentCreationRequest studentCreationRequest) {
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

    public StudentResponse getStudentById(String id) {
        Student student = studentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(ErrorCode.STUDENT_NOT_FOUND)
        );
        StudentResponse response = new StudentResponse();
        BeanUtils.copyProperties(student, response);
        return response;
    }

    @Override
    public StudentResponse getStudentByCode(String studentCode) {
        Student student = studentRepository.findByStudentCode(studentCode)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.STUDENT_NOT_FOUND));
        //Test publish message to kafka
        StudentResponse response = new StudentResponse();
        BeanUtils.copyProperties(student, response);

        //Test gui email
        NotificationEvent notificationEvent = NotificationEvent.builder()
                .chanel("EMAIL")
                .recipient(student.getEmail())
                .subject("Test email voi spring boot")
                .body("Hello, " + student.getFirstName()+" "+student.getLastName())
                .build();
        kafkaTemplate.send("test-email", notificationEvent);
        return response;
    }

    @Override
    public List<StudentResponse> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        List<StudentResponse> studentResponses = new ArrayList<>();
        students.forEach(student -> {
            StudentResponse studentResponse = new StudentResponse();
            BeanUtils.copyProperties(student, studentResponse);
            studentResponses.add(studentResponse);
        });
        return studentResponses;
    }

    @Override
    public StudentCheckResponse checkStudentExistence(Set<String> studentCodes) {
        if(studentCodes == null || studentCodes.isEmpty()) {
            return StudentCheckResponse.builder().existingCodes(Set.of()).nonExistingCodes(Set.of()).build();
        }
        List<Student> existingStudents = studentRepository.findByStudentCodes(studentCodes);
        Set<String> existingCodes = existingStudents.stream().map(Student::getStudentCode).collect(Collectors.toSet());
        Set<String> nonExistingCodes = new HashSet<>(studentCodes);
        nonExistingCodes.removeAll(existingCodes);
        return StudentCheckResponse.builder()
                .existingCodes(existingCodes)
                .nonExistingCodes(nonExistingCodes)
                .build();
    }


}
