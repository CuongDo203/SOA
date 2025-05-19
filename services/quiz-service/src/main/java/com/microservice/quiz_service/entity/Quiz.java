package com.microservice.quiz_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "quiz")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String code;

    @Column(nullable = false)
    private String title;

    @Column(name = "quiz_config_id", nullable = false)
    private String quizConfigId;

    @ElementCollection
    @CollectionTable(name = "quiz_questions", joinColumns = @JoinColumn(name = "quiz_id"))
    @Column(name = "question_id", nullable = false)
    private List<String> questionIds;

    @ElementCollection
    @CollectionTable(name = "quiz_students", joinColumns = @JoinColumn(name = "quiz_id"))
    @Column(name = "student_id", nullable = false) // Tên cột lưu ID của Student từ Student Service trong bảng join
    private List<String> assignedStudentIds;

}
