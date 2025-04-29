package com.microservice.student_service.repository;

import com.microservice.student_service.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface StudentRepository extends JpaRepository<Student, String> {

    Optional<Student> findByStudentCode(String studentCode);

    Optional<Student> findByEmail(String email);

    boolean existsByStudentCode(String studentCode);

    boolean existsByEmail(String email);

    @Query("SELECT s FROM Student s WHERE s.studentCode IN :studentCodes")
    List<Student> findByStudentCodes(@Param("studentCodes") Set<String> studentCodes);

    @Query("SELECT s FROM Student s WHERE s.email IN :emails")
    List<Student> findByEmails(@Param("emails") Set<String> emails);

}
