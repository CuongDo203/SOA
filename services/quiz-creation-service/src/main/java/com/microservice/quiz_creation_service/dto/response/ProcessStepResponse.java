package com.microservice.quiz_creation_service.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProcessStepResponse {
    private String processId;
    private String status; // Trạng thái của bước (SUCCESS, FAILED). Không phải trạng thái toàn quy trình.
    // Trạng thái toàn quy trình nằm trong Process Entity và được gửi qua SSE.
    private String message; // Thông báo kết quả của bước này
    private List<String> errors; // Chi tiết lỗi nếu status là FAILED

    // Các thông tin cuối cùng nếu bước này là bước cuối VÀ thành công (ví dụ: import students)
    private String quizId;
    private String quizCode;
}
