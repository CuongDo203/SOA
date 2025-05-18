package com.microservice.quiz_creation_service.repository;

import com.microservice.quiz_creation_service.entity.QuizCreationProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizCreationProcessRepository extends JpaRepository<QuizCreationProcess, String> {
}
