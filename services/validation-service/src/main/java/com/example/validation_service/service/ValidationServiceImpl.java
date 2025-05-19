package com.example.validation_service.service;

import com.example.validation_service.dto.*;
import com.example.validation_service.service.ValidationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

@Service
public class ValidationServiceImpl implements ValidationService {

    @Override
    public ValidationResult validateQuestions(QuestionListRequest request) {
        List<String> errors = new ArrayList<>();
        List<QuestionDto> questionDtos = request.getQuestions();
        for (int i = 0; i < questionDtos.size(); i++) {
            QuestionDto questionDto = questionDtos.get(i);
            if (questionDto.getConTent() == null || questionDto.getConTent().isEmpty()) {
                errors.add("Câu hỏi thứ " + (i + 1) + " không được để trống !");
            }

            if (questionDto.getOptA() == null || questionDto.getOptA().isEmpty()) {
                errors.add("Câu hỏi thứ " + (i + 1) + " không có câu lựa chọn A !");
            }

            if (questionDto.getOptB() == null || questionDto.getOptB().isEmpty()) {
                errors.add("Câu hỏi thứ " + (i + 1) + " không có câu lựa chọn B !");
            }

            if (questionDto.getOptC() == null || questionDto.getOptC().isEmpty()) {
                errors.add("Câu hỏi thứ " + (i + 1) + " không có câu lựa chọn C !");
            }

            if (questionDto.getOptD() == null || questionDto.getOptD().isEmpty()) {
                errors.add("Câu hỏi thứ " + (i + 1) + " không có câu lựa chọn D !");
            }

            if (questionDto.getAnswerKey() == null || questionDto.getAnswerKey().isEmpty()) {
                errors.add("Câu hỏi thứ " + (i + 1) + " phải có ít nhất một câu trả lời đúng !");
            }
        }
        return new ValidationResult(errors.isEmpty(), errors);
    }

    @Override
    public ValidationResult validateQuizConfig(QuizConfigRequest request) {
        List<String> errors = new ArrayList<>();
        if (request.getQuizName() == null || request.getQuizName().isEmpty()) {
            errors.add("Quiz name không được để trống !");
        }
        if (request.getDurationTime() == null || request.getDurationTime() <= 0) {
            errors.add("Thời lượng phải lớn hơn 0 !");
        }
        if (request.getMaxScore() == null || request.getMaxScore() <= 0) {
            errors.add("Điểm của bài thi phải lớn hơn 0 !");
        }
        if (request.getQuestionCount() == null || request.getQuestionCount() <= 0) {
            errors.add("Số lượng câu hỏi phải lớn hơn 0 !");
        }
        LocalDateTime now = LocalDateTime.now();
        if (request.getStartTime() == null) {
            errors.add("Thời gian bắt đầu không được để trống !");
        } else if (!request.getStartTime().isAfter(now)) {
            errors.add("Thời gian bắt đầu phải sau thời điểm hiện tại !");
        }
        
        // Kiểm tra thời gian kết thúc và sự hợp lệ với thời gian bắt đầu
        if (request.getFinishTime() == null) {
            errors.add("Thời gian kết thúc không được để trống !");
        } else if (request.getStartTime() != null) {
            if (!request.getFinishTime().isAfter(request.getStartTime())) {
                errors.add("Thời gian kết thúc phải sau thời gian bắt đầu !");
            } else {
                // Kiểm tra khoảng thời gian có khớp với durationTime
                long minutesBetween = ChronoUnit.MINUTES.between(request.getStartTime(), request.getFinishTime());
                if (request.getDurationTime() != null && minutesBetween != request.getDurationTime()) {
                    errors.add("Khoảng thời gian giữa bắt đầu và kết thúc phải bằng thời lượng bài thi !");
                }
            }
        }

        // Thêm các rule kiểm tra khác
        return new ValidationResult(errors.isEmpty(), errors);
    }

    @Override
    public ValidationResult validateStudents(StudentListRequest request) {
        List<String> errors = new ArrayList<>();
        List<StudentDto> students = request.getStudentCodeList();

        if (students == null || students.isEmpty())  {
            errors.add("Danh sách sinh viên không được để trống!");
        }

        if (students != null && !students.isEmpty()) {
            Set<String> seen = new HashSet<>();
            Set<String> duplicates = new HashSet<>();
            for (StudentDto student : students) {
                if (!seen.add(student.getStudentCode())) {
                    duplicates.add(student.getStudentCode());
                }
            }
            if (!duplicates.isEmpty()) {
                errors.add("Có mã sinh viên bị trùng: " + String.join(", ", duplicates));
            }
        }
        
        // Thêm các rule kiểm tra mã sinh viên, format...
        return new ValidationResult(errors.isEmpty(), errors);
    }
}