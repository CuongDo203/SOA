package com.microservice.question_service.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.microservice.question_service.exception.ErrorCode;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateQuestionRequest {

    @NotNull(message = "QUESTION_NOT_NULL")
    private String content;

    @JsonProperty("option_a")
    @NotNull(message = "OPTION_A_NOT_NULL")
    private String optionA;

    @JsonProperty("option_b")
    @NotNull(message = "OPTION_B_NOT_NULL")
    private String optionB;

    @JsonProperty("option_c")
    @NotNull(message = "OPTION_C_NOT_NULL")
    private String optionC;

    @JsonProperty("option_d")
    @NotNull(message = "OPTION_D_NOT_NULL")
    private String optionD;

    @JsonProperty("answer_key")
    @NotNull(message = "ANSWER_NOT_NULL")
    private String answerKey;

}
