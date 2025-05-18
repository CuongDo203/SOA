package com.example.quizconfig.repository;

import com.example.quizconfig.entity.QuizConfig;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuizConfigRepository extends JpaRepository<QuizConfig, String> {
    // Tìm quiz config theo mã quiz
    Optional<QuizConfig> findByQuizCode(String quizCode);

    // Kiểm tra tồn tại quiz config theo mã quiz
    boolean existsByQuizCode(String quizCode);

    // Tìm danh sách quiz config theo danh sách mã quiz
    @Query("SELECT q FROM QuizConfig q WHERE q.quizCode IN :quizCodes")
    List<QuizConfig> findByQuizCodes(@Param("quizCodes") Set<String> quizCodes);

    // // Tìm các quiz theo tên gần đúng (LIKE)
    // @Query("SELECT q FROM QuizConfig q WHERE LOWER(q.quizName) LIKE LOWER(CONCAT('%', :namePart, '%'))")
    // List<QuizConfig> searchByQuizNameLike(@Param("namePart") String namePart);
}