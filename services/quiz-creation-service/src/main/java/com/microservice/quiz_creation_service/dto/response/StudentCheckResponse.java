package com.microservice.quiz_creation_service.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentCheckResponse {
    private Set<String> existingCodes;
    private Set<String> nonExistingCodes;
    private Map<String, String> studentCodeToIdMap;
}
