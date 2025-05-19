package com.example.importservice.controller;

import com.example.importservice.dto.QuestionParsedResponse;
import com.example.importservice.dto.StudentParsedResponse; // THÊM IMPORT NÀY
import com.example.importservice.service.ImportServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/import")
@CrossOrigin(origins = "*")
public class ImportController {

    private static final Logger log = LoggerFactory.getLogger(ImportController.class);

    @Autowired
    private ImportServiceImpl importService;

    @PostMapping("/questions/excel")
    public ResponseEntity<?> uploadAndParseQuestionsExcel(@RequestPart("file") MultipartFile excelFile) {
        if (excelFile.isEmpty()) {
            return ResponseEntity.badRequest().body("Please select an Excel file for questions.");
        }
        String contentType = excelFile.getContentType();
        if (contentType == null ||
                !(contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") ||
                        contentType.equals("application/vnd.ms-excel"))) {
            log.warn("Invalid file type for questions: {}", contentType);
            return ResponseEntity.badRequest().body("Invalid file type for questions. Please upload an Excel file (.xls or .xlsx).");
        }
        try {
            log.info("Received file for question import: {}", excelFile.getOriginalFilename());
            List<QuestionParsedResponse> parsedQuestions = importService.parseQuestionsFromExcel(excelFile);
            if (parsedQuestions.isEmpty()) {
                log.info("No questions were parsed from the file: {}", excelFile.getOriginalFilename());
                return ResponseEntity.ok("File processed (questions), but no valid questions found or the file was empty after the header.");
            }
            log.info("Successfully parsed {} questions from file {}.", parsedQuestions.size(), excelFile.getOriginalFilename());
            return ResponseEntity.ok(parsedQuestions);
        } catch (IllegalArgumentException e) {
            log.error("Invalid argument during question import: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            log.error("IO error during question import: {}", excelFile.getOriginalFilename(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing the question Excel file: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error during question import: {}", excelFile.getOriginalFilename(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred during question import: " + e.getMessage());
        }
    }

    @PostMapping("/students/excel")
    public ResponseEntity<?> uploadAndParseStudentsExcel(@RequestPart("file") MultipartFile excelFile) {
        if (excelFile.isEmpty()) {
            return ResponseEntity.badRequest().body("Please select an Excel file for students.");
        }

        String contentType = excelFile.getContentType();
        if (contentType == null ||
                !(contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") || // .xlsx
                        contentType.equals("application/vnd.ms-excel") || // .xls
                        contentType.equals("text/csv") || // .csv
                        excelFile.getOriginalFilename().toLowerCase().endsWith(".csv") // Kiểm tra đuôi file cho csv
                )) {
            log.warn("Invalid file type for students: {}", contentType);
            return ResponseEntity.badRequest().body("Invalid file type for students. Please upload an Excel (.xls, .xlsx) or CSV file.");
        }

        try {
            log.info("Received file for student import: {}", excelFile.getOriginalFilename());
            List<StudentParsedResponse> parsedStudents = importService.parseStudentsFromExcel(excelFile); // Gọi phương thức mới

            if (parsedStudents.isEmpty()) {
                log.info("No students were parsed from the file: {}", excelFile.getOriginalFilename());
                return ResponseEntity.ok("File processed (students), but no valid students found or the file was empty after the header.");
            }

            log.info("Successfully parsed {} students from file {}.", parsedStudents.size(), excelFile.getOriginalFilename());
            return ResponseEntity.ok(parsedStudents);

        } catch (IllegalArgumentException e) {
            log.error("Invalid argument during student import: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            log.error("IO error during student import: {}", excelFile.getOriginalFilename(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing the student Excel file: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error during student import: {}", excelFile.getOriginalFilename(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred during student import: " + e.getMessage());
        }
    }
}