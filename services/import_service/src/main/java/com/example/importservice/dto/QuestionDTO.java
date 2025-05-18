package com.example.importservice.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data // Bao gồm @Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@Builder // Nếu bạn muốn dùng builder pattern để tạo QuestionDTO
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuestionDTO {

    // private Long id; // Có thể được gán bởi service khác sau khi tạo
    private String content;
    private String ansA;
    private String ansB;
    private String ansC;
    private String ansD;
    private String correctAns; // Ví dụ: "A", "B", "C", "D"
//    private Long subjectId;
//    private Long levelId;

    // Không cần viết thủ công getters, setters, toString, v.v. nữa
    // Lombok sẽ tự động tạo chúng.
}