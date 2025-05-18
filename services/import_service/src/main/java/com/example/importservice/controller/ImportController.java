package com.example.importservice.controller;

import com.example.importservice.dto.QuestionDTO;
import com.example.importservice.service.ImportServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/import") // Base path cho tất cả các endpoint trong controller này
public class ImportController {

    @Autowired // Tiêm ImportServiceImpl vào đây
    private ImportServiceImpl importService;

    @PostMapping("/questions") // Endpoint: POST /api/v1/import/questions
    public ResponseEntity<List<QuestionDTO>> importQuestions(@Valid @RequestBody List<QuestionDTO> questions) {
        // @Valid sẽ kích hoạt validation cho danh sách QuestionDTO và từng QuestionDTO bên trong
        // @RequestBody nghĩa là danh sách questions sẽ được lấy từ body của HTTP request (dưới dạng JSON)

        if (questions == null || questions.isEmpty()) {
            // Trả về bad request nếu không có câu hỏi nào được gửi
            return ResponseEntity.badRequest().body(null); // Hoặc một thông báo lỗi cụ thể
        }

        List<QuestionDTO> importedQuestions = importService.importQuestions(questions);

        // Quyết định response code dựa trên kết quả import
        if (importedQuestions.size() == questions.size()) {
            // Tất cả câu hỏi được import thành công
            return ResponseEntity.ok(importedQuestions);
        } else if (!importedQuestions.isEmpty()) {
            // Một vài câu hỏi được import, một vài câu không
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(importedQuestions);
        } else {
            // Không câu hỏi nào được import (có thể do lỗi toàn bộ hoặc input rỗng đã xử lý ở trên)
            // Bạn có thể trả về INTERNAL_SERVER_ERROR nếu lỗi xảy ra ở service,
            // hoặc một status khác tùy theo logic xử lý lỗi của bạn.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>()); // Trả về danh sách rỗng
        }
    }
}