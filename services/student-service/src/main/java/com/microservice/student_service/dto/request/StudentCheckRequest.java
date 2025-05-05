package com.microservice.student_service.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentCheckRequest {

    @NotEmpty(message = "SET_STUDENT_CODE_EMPTY")
    private Set<String> studentCodes;

}
