package com.example.quizconfig.repository;

import com.example.quizconfig.entity.QuizConfig;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuizConfigRepository extends JpaRepository<QuizConfig, String> {

}