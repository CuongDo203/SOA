package com.example.validation_service.service;

import com.example.validation_service.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

@Service
@Slf4j
public class ValidationServiceImpl implements ValidationService {

    @Override
    public ValidationResult validateQuestions(QuestionListRequest request) {
        List<String> errors = new ArrayList<>();
        List<QuestionParsedResponse> questionParsedResponses = request.getQuestions();
        for (int i = 0; i < questionParsedResponses.size(); i++) {
            QuestionParsedResponse questionParsedResponse = questionParsedResponses.get(i);
            if (questionParsedResponse.getContent() == null || questionParsedResponse.getContent().isEmpty()) {
                errors.add("Câu hỏi thứ " + (i + 1) + " không được để trống !");
            }

            if (questionParsedResponse.getOptionA() == null || questionParsedResponse.getOptionA().isEmpty()) {
                errors.add("Câu hỏi thứ " + (i + 1) + " không có câu lựa chọn A !");
            }

            if (questionParsedResponse.getOptionB() == null || questionParsedResponse.getOptionB().isEmpty()) {
                errors.add("Câu hỏi thứ " + (i + 1) + " không có câu lựa chọn B !");
            }

            if (questionParsedResponse.getOptionC() == null || questionParsedResponse.getOptionC().isEmpty()) {
                errors.add("Câu hỏi thứ " + (i + 1) + " không có câu lựa chọn C !");
            }

            if (questionParsedResponse.getOptionD() == null || questionParsedResponse.getOptionD().isEmpty()) {
                errors.add("Câu hỏi thứ " + (i + 1) + " không có câu lựa chọn D !");
            }

            if (questionParsedResponse.getAnswerKey() == null || questionParsedResponse.getAnswerKey().isEmpty()) {
                errors.add("Câu hỏi thứ " + (i + 1) + " phải có ít nhất một câu trả lời đúng !");
            }
        }
        return new ValidationResult(errors.isEmpty(), errors);
    }

    @Override
    public ValidationResult validateQuizConfig(QuizConfigDTO request) {
        List<String> errors = new ArrayList<>();
        log.info("durationTime: " + request.getDurationMinutes());
        log.info("maxScore: " + request.getMaxScore());
        log.info("startTime: " + request.getStart());
        log.info("finishTime: " + request.getEnd());

        if (request.getDurationMinutes() == null || request.getDurationMinutes() <= 0) {
            errors.add("Thời lượng phải lớn hơn 0 !");
        }
        if (request.getMaxScore() == null || request.getMaxScore() <= 0) {
            errors.add("Điểm của bài thi phải lớn hơn 0 !");
        }
        if (request.getQuestionCount() == null || request.getQuestionCount() <= 0) {
            errors.add("Số lượng câu hỏi phải lớn hơn 0 !");
        }
        LocalDateTime now = LocalDateTime.now();
        if (request.getStart() == null) {
            errors.add("Thời gian bắt đầu không được để trống !");
        } else if (!request.getStart().isAfter(now)) {
            errors.add("Thời gian bắt đầu phải sau thời điểm hiện tại !");
        }
        
        // Kiểm tra thời gian kết thúc và sự hợp lệ với thời gian bắt đầu
        if (request.getEnd() == null) {
            errors.add("Thời gian kết thúc không được để trống !");
        } else if (request.getStart() != null) {
            if (!request.getEnd().isAfter(request.getStart())) {
                errors.add("Thời gian kết thúc phải sau thời gian bắt đầu !");
            } else {
                // Kiểm tra khoảng thời gian có khớp với durationTime
                long minutesBetween = ChronoUnit.MINUTES.between(request.getStart(), request.getEnd());
                if (request.getDurationMinutes() != null && minutesBetween != request.getDurationMinutes()) {
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
        List<StudentParsedResponse> students = request.getStudents();

        if (students == null || students.isEmpty())  {
            errors.add("Danh sách sinh viên không được để trống!");
        }

        if (students != null && !students.isEmpty()) {
            Set<String> seen = new HashSet<>();
            Set<String> duplicates = new HashSet<>();
            for (StudentParsedResponse student : students) {
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