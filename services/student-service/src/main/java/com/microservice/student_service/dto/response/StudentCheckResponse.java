package com.microservice.student_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentCheckResponse {
    private Set<String> existingCodes;
    private Set<String> nonExistingCodes;
}
