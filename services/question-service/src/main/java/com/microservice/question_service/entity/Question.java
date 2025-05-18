package com.microservice.question_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "question")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, columnDefinition = "TEXT", name = "option_a")
    private String optionA;

    @Column(nullable = false, columnDefinition = "TEXT", name = "option_b")
    private String optionB;

    @Column(nullable = false, columnDefinition = "TEXT", name = "option_c")
    private String optionC;

    @Column(nullable = false, columnDefinition = "TEXT", name = "option_d")
    private String optionD;

    @Column(nullable = false, columnDefinition = "TEXT", name = "answer_key")
    private String answerKey;

}
