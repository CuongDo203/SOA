package com.microservice.quiz_creation_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionParsedResponse {
    private String question;
    private String answer;
    private String[] options;

}
