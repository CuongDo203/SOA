package com.example.validation_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
// import jakarta.validation.constraints.Email;
// import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentParsedResponse {
     @JsonProperty("student_code")
    String studentCode;

     @JsonProperty("first_name")
    String firstName;

     @JsonProperty("last_name")
    String lastName;

    String email;

    @JsonProperty("class_name")
    String className;
}
