package com.example.validation_service.service;

import com.example.validation_service.dto.*;
import com.example.validation_service.service.ValidationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

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
        if (request.getStudentCodeList() == null || request.getStudentCodeList().isEmpty()) {
            errors.add("Danh sách mã sinh viên không được để trống !");
        }

        if (request.getStudentNameList() == null || request.getStudentNameList().isEmpty()) {
            errors.add("Danh sách mã sinh viên không được để trống !");
        }
        // Thêm các rule kiểm tra mã sinh viên, format...
        return new ValidationResult(errors.isEmpty(), errors);
    }
}