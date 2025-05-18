package com.microservice.notification_service.event;

import com.microservice.event.dto.NotificationEvent;
import com.microservice.event.dto.SendQuizCodeEvent;
import com.microservice.notification_service.service.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventConsumer {

    EmailService emailService;

    @KafkaListener(topics = "test-email")
    public void testEmail(SendQuizCodeEvent event) {
        log.info("Received message: "+ event);
        emailService.sendEmail(event.getRecipient(), event.getSubject(), event.getBody(),
                false, null);

    }

}
