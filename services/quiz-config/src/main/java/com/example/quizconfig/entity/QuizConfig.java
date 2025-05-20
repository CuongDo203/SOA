package com.example.quizconfig.entity;

// import java.sql.Date;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class QuizConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Integer durationMinutes; // Thời gian làm bài (phút)
    private Integer questionCount;   // Số lượng câu hỏi
    private Double maxScore;         // Điểm tối đa
    private String rules;            // Quy định khác (nếu có)

    private LocalDateTime start;
    private LocalDateTime end;
}