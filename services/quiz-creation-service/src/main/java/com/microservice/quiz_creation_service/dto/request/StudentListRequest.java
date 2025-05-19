package com.microservice.quiz_creation_service.dto.request;
import com.microservice.quiz_creation_service.dto.response.StudentParsedResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentListRequest {
    private List<StudentParsedResponse> students;
}
