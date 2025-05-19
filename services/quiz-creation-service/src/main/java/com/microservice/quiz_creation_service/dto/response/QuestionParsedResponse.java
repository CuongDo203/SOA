package com.microservice.quiz_creation_service.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionParsedResponse {
    private String content;
    @JsonProperty("option_a")
    private String optionA;
    @JsonProperty("option_b")
    private String optionB;
    @JsonProperty("option_c")
    private String optionC;
    @JsonProperty("option_d")
    private String optionD;
    @JsonProperty("answer_key")
    private String answerKey;
}
