package com.microservice.quiz_creation_service.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuizConfigDTO {
    private String id;
    private Integer durationMinutes; // Thời gian làm bài (phút)
    private Integer questionCount;   // Số lượng câu hỏi
    private Double maxScore;         // Điểm tối đa
    private String rules;            // Quy định khác (nếu có)

    private LocalDateTime start;
    private LocalDateTime end;
}
