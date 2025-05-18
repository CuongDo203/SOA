package com.example.importservice.controller;

import com.example.importservice.dto.QuestionDTO;
import com.example.importservice.service.ImportServiceImpl; // Hoặc interface nếu có
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/import") // Hoặc một path phù hợp hơn
public class ImportController {

    private static final Logger log = LoggerFactory.getLogger(ImportController.class);

    @Autowired
    private ImportServiceImpl importService; // Sử dụng implementation trực tiếp hoặc interface

    // Client để gọi QuizCreationService (ví dụ dùng Feign)
    // @Autowired
    // private QuizCreationServiceClient quizCreationServiceClient;

    @PostMapping("/questions/excel")
    public ResponseEntity<?> uploadAndParseQuestionsExcel(@RequestParam("file") MultipartFile excelFile) {
        if (excelFile.isEmpty()) {
            return ResponseEntity.badRequest().body("Please select an Excel file to upload.");
        }

        // Kiểm tra kiểu file nếu cần
        String contentType = excelFile.getContentType();
        if (contentType == null ||
                !(contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") || // .xlsx
                        contentType.equals("application/vnd.ms-excel"))) { // .xls
            log.warn("Invalid file type uploaded: {}", contentType);
            return ResponseEntity.badRequest().body("Invalid file type. Please upload an Excel file (.xls or .xlsx).");
        }

        try {
            log.info("Received file for question import: {}", excelFile.getOriginalFilename());
            List<QuestionDTO> parsedQuestions = importService.parseQuestionsFromExcel(excelFile);

            if (parsedQuestions.isEmpty()) {
                log.info("No questions were parsed from the file: {}", excelFile.getOriginalFilename());
                return ResponseEntity.ok("File processed, but no valid questions found or the file was empty after the header.");
            }

            log.info("Successfully parsed {} questions from file {}.", parsedQuestions.size(), excelFile.getOriginalFilename());

            // BƯỚC TIẾP THEO: Gửi `parsedQuestions` đến Quiz Creation Service
            // Ví dụ:
            // ResponseEntity<String> creationResponse = quizCreationServiceClient.createQuizWithQuestions(parsedQuestions);
            // return creationResponse;

            // Hiện tại, chỉ trả về danh sách đã parse để kiểm tra
            return ResponseEntity.ok(parsedQuestions);

        } catch (IllegalArgumentException e) {
            log.error("Invalid argument during question import: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            log.error("IO error during question import: {}", excelFile.getOriginalFilename(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing the Excel file: " + e.getMessage());
        } catch (Exception e) { // Bắt các lỗi không lường trước khác
            log.error("Unexpected error during question import: {}", excelFile.getOriginalFilename(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }
}