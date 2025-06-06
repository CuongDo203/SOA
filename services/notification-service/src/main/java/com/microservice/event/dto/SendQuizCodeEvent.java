package com.microservice.event.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SendQuizCodeEvent {
    private String quizCode;
    private List<String> studentIds;
}
