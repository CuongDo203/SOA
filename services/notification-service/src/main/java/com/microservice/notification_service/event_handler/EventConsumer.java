package com.microservice.notification_service.event_handler;

import com.microservice.event.dto.SendQuizCodeEvent;
import com.microservice.notification_service.clients.StudentServiceClient;
import com.microservice.notification_service.dto.request.StudentIdsRequest;
import com.microservice.notification_service.dto.response.ApiResponse;
import com.microservice.notification_service.dto.response.StudentResponse;
import com.microservice.notification_service.service.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.retry.RetryException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventConsumer {

    EmailService emailService;
    StudentServiceClient studentServiceClient;

    @RetryableTopic(
            attempts = "4",
            backoff = @Backoff(delay = 1000, multiplier = 2),
            autoCreateTopics = "true",
            dltStrategy = DltStrategy.FAIL_ON_ERROR,
            include = {RetryException.class, RuntimeException.class}
    )
    @KafkaListener(topics = "quiz-creation-notification-topic", groupId = "notification-group")
    public void handleQuizCreationEvent(SendQuizCodeEvent event) {
        log.info("Received quiz creation event for quiz code: {}", event.getQuizCode());

        // Lấy danh sách ID sinh viên từ event
        List<String> studentIds = event.getStudentIds();

        // Gọi Student Service để lấy thông tin chi tiết của sinh viên
        ApiResponse<List<StudentResponse>> response = studentServiceClient.getStudentsByIds(StudentIdsRequest
                .builder()
                .ids(studentIds)
                .build());
        if(response.getCode() == 1000) {
            List<StudentResponse> studentResponses = response.getResult();
            studentResponses.forEach(student ->
                sendQuizNotificationEmail(student, event.getQuizCode()));
        }
    }

    private void sendQuizNotificationEmail(StudentResponse student, String quizCode) {
        // Chuẩn bị nội dung email
        String subject = "New Quiz Assignment";
        String content = String.format(
                "Dear %s %s,\n\nYou have been assigned to a new quiz with code: %s.\n\nPlease login to your account to take the quiz.\n\nBest regards,\nQuiz System",
                student.getFirstName(),
                student.getLastName(),
                quizCode
        );

        // Gửi email
        emailService.sendEmail(student.getEmail(), subject, content, false, null);
        log.info("Sent quiz notification email to: {}", student.getEmail());
    }

}
