package com.microservice.quiz_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizDTO {

    private String title;
    private String quizConfigId;
    private List<String> questionIds;
    private List<String> assignedStudentIds;

}
