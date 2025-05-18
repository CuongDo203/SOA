package com.microservice.quiz_creation_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateQuizRequest {
    private String quizTitle;
    private byte[] rawQuizDataBytes;

}
