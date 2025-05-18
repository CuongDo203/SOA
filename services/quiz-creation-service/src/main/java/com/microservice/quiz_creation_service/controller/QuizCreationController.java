package com.microservice.quiz_creation_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.event.dto.SendQuizCodeEvent;
import com.microservice.quiz_creation_service.dto.request.CreateQuizRequest;
import com.microservice.quiz_creation_service.dto.response.ApiResponse;
import com.microservice.quiz_creation_service.dto.response.CreateProcessResponse;
import com.microservice.quiz_creation_service.dto.response.CreateQuizResponse;
import com.microservice.quiz_creation_service.dto.response.ProcessStepResponse;
import com.microservice.quiz_creation_service.service.QuizCreationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/quiz-creation")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class QuizCreationController {

    QuizCreationService quizCreationService;
    KafkaTemplate<String, Object> kafkaTemplate;

    @PostMapping("/start")
    public ApiResponse startQuizCreationProcess() {
        CreateProcessResponse response = quizCreationService.startQuizCreationProcess();
        return ApiResponse.builder()
                .message(response.getMessage())
                .result(response)
                .build();
    }


    @PostMapping(value = "/{processId}/import/questions", consumes = {"multipart/form-data"})
    public ApiResponse importQuestions(@PathVariable String processId,
                                       @RequestParam("questionFile") MultipartFile questionFile) {
        ProcessStepResponse response = quizCreationService.importQuestions(processId, questionFile);
        if("FAILED".equals(response.getStatus())) {

        }
        return ApiResponse.builder()
                .message(response.getMessage())
                .result(response)
                .build();
    }

//    @PostMapping("/create")
//    public ResponseEntity<ApiResponse> createQuiz(@RequestParam(value = "questionFile") MultipartFile questionFile,
//                                                  @RequestParam(value = "studentFile") MultipartFile studentFile,
//                                                  @RequestParam("config") String configJson
//                                                  ) {
//        try {
//            if (questionFile != null) {
//                System.out.println("Question File Name: " + questionFile.getOriginalFilename());
//                System.out.println("Question File Size: " + questionFile.getSize() + " bytes");
//                System.out.println("Question File Content Type: " + questionFile.getContentType());
//            } else {
//                System.out.println("No question file received");
//            }
//
//            // In thông tin về file sinh viên
//            if (studentFile != null) {
//                System.out.println("Student File Name: " + studentFile.getOriginalFilename());
//                System.out.println("Student File Size: " + studentFile.getSize() + " bytes");
//                System.out.println("Student File Content Type: " + studentFile.getContentType());
//            } else {
//                System.out.println("No student file received");
//            }
//            ObjectMapper objectMapper = new ObjectMapper();
//            // Chuyển đổi chuỗi JSON cấu hình thành Map để dễ xử lý
//            Map<String, Object> config = objectMapper.readValue(configJson, HashMap.class);
//            System.out.println("Configuration received:");
//            config.forEach((key, value) -> System.out.println("  " + key + ": " + value));
//            // Tạo mã quiz giả lập (trong thực tế, có thể từ database)
//            String quizCode = "QZ-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
//
//            // Trả về phản hồi thành công
//            Map<String, Object> responseData = new HashMap<>();
//            responseData.put("quizCode", quizCode);
//            responseData.put("message", "Quiz created successfully");
//
//            return ResponseEntity.ok(ApiResponse.builder()
//                    .message("Quiz created successfully")
//                    .result(responseData)
//                    .build());
//        }
//        catch (Exception e) {
//            log.error(e.getMessage());
//            return ResponseEntity.badRequest().body(ApiResponse.builder()
//                    .message("Failed to create quiz: " + e.getMessage())
//                    .build());
//        }
//
//    }

    @PostMapping("/create-quiz")
    public ApiResponse createQuiz(@RequestBody CreateQuizRequest createQuizRequest) {

        CreateQuizResponse response = quizCreationService.createQuiz(createQuizRequest);
        return ApiResponse.builder()
                .message("Quiz created successfully!")
                .result(response)
                .build();
    }
}
