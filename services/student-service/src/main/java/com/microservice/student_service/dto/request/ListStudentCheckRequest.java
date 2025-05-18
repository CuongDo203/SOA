package com.microservice.student_service.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListStudentCheckRequest {

    @NotEmpty(message = "SET_STUDENT_CHECK_EMPTY")
    private List<StudentCheckRequest> studentChecks;

}
