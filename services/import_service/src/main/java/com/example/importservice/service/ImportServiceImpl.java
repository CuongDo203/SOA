package com.example.importservice.service;

import com.example.importservice.client.QuestionServiceClient;
import com.example.importservice.dto.QuestionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service // Đánh dấu đây là một Spring service bean
public class ImportServiceImpl {

    private static final Logger log = LoggerFactory.getLogger(ImportServiceImpl.class);

    @Autowired // Tiêm QuestionServiceClient vào đây
    private QuestionServiceClient questionServiceClient;

    public List<QuestionDTO> importQuestions(List<QuestionDTO> questionsToImport) {
        List<QuestionDTO> importedQuestions = new ArrayList<>();
        if (questionsToImport == null) {
            return importedQuestions; // Trả về danh sách rỗng nếu input là null
        }

        for (QuestionDTO questionDTO : questionsToImport) {
            try {
                // Ở đây có thể thêm logic validate danh sách câu hỏi trước khi gửi
                // Ví dụ: kiểm tra trùng lặp, định dạng,...
                log.info("Attempting to import question: {}", questionDTO.getContent());

                // Gọi đến QuestionService thông qua Feign client
                ResponseEntity<QuestionDTO> response = questionServiceClient.createQuestion(questionDTO);

                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    importedQuestions.add(response.getBody());
                    log.info("Successfully imported question ID: {}", Objects.requireNonNull(response.getBody()).getId());
                } else {
                    log.error("Failed to import question '{}'. Status: {}, Body: {}",
                            questionDTO.getContent(), response.getStatusCode(), response.getBody());
                    // Có thể ném exception hoặc thêm vào danh sách lỗi để trả về cho client
                }
            } catch (Exception e) {
                log.error("Error importing question '{}': {}", questionDTO.getContent(), e.getMessage(), e);
                // Xử lý exception từ Feign client (ví dụ: QuestionService không hoạt động, network error,...)
                // Có thể ném một custom exception hoặc thêm thông tin lỗi
            }
        }
        return importedQuestions;
    }
}