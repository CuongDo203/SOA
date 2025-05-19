package com.microservice.quiz_creation_service.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateQuizResponse {
    private String id;
    private String code;
    private String title;
    private List<String> studentIds;
    private String quizConfigId;
    private List<String> questionIds;
}
