package com.example.importservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data // Bao gồm @Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@Builder // Nếu bạn muốn dùng builder pattern để tạo QuestionParsedResponse
@FieldDefaults(level = AccessLevel.PRIVATE)
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
    private String answerKey; // Ví dụ: "A", "B", "C", "D"

}