package com.microservice.student_service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentCheckRequest {

    @JsonProperty("student_code")
    @NotBlank(message = "STUDENT_CODE_REQUIRED")
    String studentCode;

    @JsonProperty("first_name")
    @NotBlank(message = "FIRST_NAME_REQUIRED")
    String firstName;

    @JsonProperty("last_name")
    @NotBlank(message = "LAST_NAME_REQUIRED")
    String lastName;

    @Email(message = "EMAIL_NOT_VALID")
    String email;

    @JsonProperty("class_name")
    String className;

}
