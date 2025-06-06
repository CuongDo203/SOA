package com.microservice.quiz_creation_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizParsedResponse {
    private Integer durationMinutes; // Thời gian làm bài (phút)
    private Integer questionCount;   // Số lượng câu hỏi
    private Double maxScore;         // Điểm tối đa
    private String rules;            // Quy định khác (nếu có)

    private LocalDateTime start;
    private LocalDateTime end;
}
