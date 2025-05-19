package com.microservice.quiz_service.repository;

import com.microservice.quiz_service.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, String> {
    Optional<Quiz> findByCode(String code);
    boolean existsByCode(String code);
}
