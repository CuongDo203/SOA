package com.microservice.event.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SendQuizCodeEvent {
    String recipient;
    String subject;
    String body;
}
