package com.microservice.student_service.entity;

import com.microservice.student_service.dto.request.StudentCreationRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@Table(name = "student")
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "student_code", nullable = false, unique = true)
    @NotBlank(message = "STUDENT_CODE_REQUIRED")
    private String studentCode;

    @Column(name = "first_name", nullable = false)
    @NotBlank(message = "FIRST_NAME_REQUIRED")
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @NotBlank(message = "LAST_NAME_REQUIRED")
    private String lastName;

    @Email(message = "EMAIL_NOT_VALID")
    @Column(unique = true)
    private String email;

    @Column(name = "class_name")
    private String className;

    public static Student fromStudentCreationRequest(StudentCreationRequest request) {
        return Student.builder()
                .studentCode(request.getStudentCode())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .className(request.getClassName())
                .build();
    }
}
