package com.example.validation_service.service;

import com.example.validation_service.dto.*;
import com.example.validation_service.service.ValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ValidationServiceImpl implements ValidationService {

    @Override
    public ValidationResult validateQuestions(QuestionListRequest request) {
        List<String> errors = new ArrayList<>();
        List<QuestionDto> questionDtos = request.getQuestions();
        for (int i = 0; i < questionDtos.size(); i++) {
            QuestionDto questionDto = questionDtos.get(i);
            if (questionDto.getContent() == null || questionDto.getContent().isEmpty()) {
                errors.add("Câu hỏi thứ " + (i + 1) + " không được để trống !");
            }

            if (questionDto.getOptionA() == null || questionDto.getOptionA().isEmpty()) {
                errors.add("Câu hỏi thứ " + (i + 1) + " không có câu lựa chọn A !");
            }

            if (questionDto.getOptionB() == null || questionDto.getOptionB().isEmpty()) {
                errors.add("Câu hỏi thứ " + (i + 1) + " không có câu lựa chọn B !");
            }

            if (questionDto.getOptionC() == null || questionDto.getOptionC().isEmpty()) {
                errors.add("Câu hỏi thứ " + (i + 1) + " không có câu lựa chọn C !");
            }

            if (questionDto.getOptionD() == null || questionDto.getOptionD().isEmpty()) {
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
//        if (request.getQuizName() == null || request.getQuizName().isEmpty()) {
//            errors.add("Quiz name không được để trống !");
//        }
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