package main.java.com.example.quizconfig.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class QuizConfigDTO {
    private String id;
    private String quizName;
    private Integer durationMinutes;
    private Integer questionCount;
    private Double maxScore;
    private String rules;
    private String quizCode;
}
