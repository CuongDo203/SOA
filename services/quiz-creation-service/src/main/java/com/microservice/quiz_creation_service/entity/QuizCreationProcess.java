//package com.microservice.quiz_creation_service.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//@Entity
//@Table(name = "quiz_creation_process")
//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//public class QuizCreationProcess {
//    public enum Status {
//        NOT_STARTED, PENDING_QUESTIONS, QUESTIONS_VALIDATED,
//        PENDING_CONFIG, CONFIG_VALIDATED, PENDING_STUDENTS,
//        STUDENTS_VALIDATED, SAVING_ENTITIES, CREATING_QUIZ,
//        COMPLETED, FAILED
//    }
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
//    private String processId;
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private Status status;
//
//    @Column(columnDefinition = "TEXT")
//    private String validatedQuestionsDataJson;
//
//    @Column(columnDefinition = "TEXT")
//    private String validatedStudentIdentifiersJson;
//
//    private String finalQuizId;
//    private String finalQuizCode;
//
//    @Column(columnDefinition = "TEXT")
//    private String errorDetails;
//
//    @Column(nullable = false)
//    private LocalDateTime createdAt;
//
//    @Column(nullable = false)
//    private LocalDateTime updatedAt;
//
//    @PrePersist
//    protected void onCreate() {
//        if (this.processId == null) {
//            this.processId = UUID.randomUUID().toString();
//        }
//        if (this.createdAt == null) {
//            this.createdAt = LocalDateTime.now();
//        }
//        // updatedAt cũng set khi create
//        if (this.updatedAt == null) {
//            this.updatedAt = LocalDateTime.now();
//        }
//        if (this.status == null) {
//            this.status = Status.NOT_STARTED; // Trang thai ban dau
//        }
//    }
//
//    @PreUpdate
//    protected void onUpdate() {
//        this.updatedAt = LocalDateTime.now();
//    }
//}
