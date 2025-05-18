package com.microservice.quiz_creation_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.quiz_creation_service.clients.ImportServiceClient;
import com.microservice.quiz_creation_service.dto.response.CreateProcessResponse;
import com.microservice.quiz_creation_service.dto.response.ProcessStepResponse;
import com.microservice.quiz_creation_service.dto.response.QuestionParsedResponse;
import com.microservice.quiz_creation_service.entity.QuizCreationProcess;
import com.microservice.quiz_creation_service.exception.ErrorCode;
import com.microservice.quiz_creation_service.exception.InvalidProcessStateException;
import com.microservice.quiz_creation_service.exception.ResourceNotFoundException;
import com.microservice.quiz_creation_service.repository.QuizCreationProcessRepository;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class QuizCreationServiceImpl implements QuizCreationService{

    QuizCreationProcessRepository processRepository;
    ObjectMapper objectMapper;
    ImportServiceClient importServiceClient;

    @Transactional
    public CreateProcessResponse startQuizCreationProcess() {
        QuizCreationProcess process = new QuizCreationProcess();
        process.setStatus(QuizCreationProcess.Status.PENDING_QUESTIONS);
        QuizCreationProcess savedProcess = processRepository.save(process);
        log.info("QuizCreationService: Start with id: {}", savedProcess.getProcessId());
        CreateProcessResponse response = CreateProcessResponse.builder()
                .processId(savedProcess.getProcessId())
                .status(savedProcess.getStatus().name())
                .message("Quiz creation process started. Please import questions.")
                .build();
        return response;
    }

    @Transactional
    public ProcessStepResponse importQuestions(String processId, MultipartFile questionFile) {
        QuizCreationProcess process = processRepository.findById(processId).orElseThrow(
                () -> new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND_EXCEPTION));

        if(process.getStatus() != QuizCreationProcess.Status.NOT_STARTED &&
            process.getStatus() != QuizCreationProcess.Status.PENDING_QUESTIONS) {
            throw new InvalidProcessStateException(ErrorCode.INVALID_PROCESS_STATE_EXCEPTION);
        }
        process.setStatus(QuizCreationProcess.Status.PENDING_QUESTIONS);
        //Gọi import service để parse câu hỏi
        List<QuestionParsedResponse> questions;
        try {
            questions = importServiceClient.importQuestions(questionFile);
            // Kiểm tra kết quả từ Import Service nếu cần (ví dụ: list rỗng có hợp lệ không)
            if (questions == null || questions.isEmpty()) {
                throw new RuntimeException("Import service returned no questions or null list.");
            }
        } catch (FeignException e) {
            // Lỗi từ Import Service (bao gồm cả lỗi parse/format)
            String errorMessage = "Import Service failed during question parsing: " + e.getMessage();
            // Cập nhật trạng thái FAILED và chi tiết lỗi vào bản ghi quy trình
            process.setStatus(QuizCreationProcess.Status.FAILED);
            process.setErrorDetails(errorMessage);
            processRepository.save(process); // Lưu trạng thái FAILED
//            statusEmitterService.emitStatusUpdate(process); // Phát tín hiệu cập nhật trạng thái

            // Trả về phản hồi lỗi cho bước này
            ProcessStepResponse response = ProcessStepResponse.builder()
                    .processId(processId)
                    .status("FAILED")
                    .message("Import questions failed.")
                    .errors(List.of(errorMessage))
                    .build();
            return response;
        }
//        catch (IOException e) {
//            // Lỗi đọc byte[]
//            String errorMessage = "Failed to read question file bytes: " + e.getMessage();
//            process.setStatus(QuizCreationProcess.Status.FAILED);
//            process.setErrorDetails(errorMessage);
//            processRepository.save(process);
////            statusEmitterService.emitStatusUpdate(process);
//            ProcessStepResponse response = ProcessStepResponse.builder()
//                    .processId(processId)
//                    .status("FAILED")
//                    .message("Failed to read question file.")
//                    .errors(List.of(errorMessage))
//                    .build();
//            return response;
//        }
//        catch (Exception e) { // Bắt các lỗi khác từ Import Service hoặc trong quá trình chuẩn bị
//            String errorMessage = "Unexpected error during question import: " + e.getMessage();
//            process.setStatus(QuizCreationProcess.Status.FAILED);
//            process.setErrorDetails(errorMessage);
//            processRepository.save(process);
//            statusEmitterService.emitStatusUpdate(process);
//            return new ProcessStepResponse(processId, "FAILED", "An unexpected error occurred during question import.", List.of(errorMessage));
//        }

        //Gọi validation service
//        ValidationResult validationResult;
//        try {
//            validationResult = validationServiceClient.validateQuestions(questions);
//            // Kiểm tra kết quả từ Validation Service
//            if (validationResult == null) {
//                throw new RuntimeException("Validation service returned null result for questions.");
//            }
//        } catch (FeignException e) {
//            // Lỗi từ Validation Service
//            String errorMessage = "Validation Service failed during question validation: " + getErrorMessageFromFeignException(e);
//            process.setStatus(QuizCreationProcess.Status.FAILED);
//            process.setErrorDetails(errorMessage);
//            processRepository.save(process); // Lưu trạng thái FAILED
//            statusEmitterService.emitStatusUpdate(process); // Phát tín hiệu cập nhật trạng thái
//
//            return new ProcessStepResponse(processId, "FAILED", "Validate questions failed.", List.of(errorMessage));
//        } catch (Exception e) { // Bắt các lỗi khác
//            String errorMessage = "Unexpected error during question validation: " + e.getMessage();
//            process.setStatus(QuizCreationProcess.Status.FAILED);
//            process.setErrorDetails(errorMessage);
//            processRepository.save(process);
//            statusEmitterService.emitStatusUpdate(process);
//            return new ProcessStepResponse(processId, "FAILED", "An unexpected error occurred during question validation.", List.of(errorMessage));
//        }
        // Kiểm tra kết quả xác thực
//        if (!validationResult.isValid()) {
//            // Xác thực lỗi
//            String errorMessages = String.join("; ", validationResult.getErrors());
//            String userMessage = "Question validation failed.";
//            System.err.println("Question validation failed for process " + processId + ": " + errorMessages);
//
//            process.setStatus(QuizCreationProcess.Status.FAILED);
//            process.setErrorDetails(userMessage + " " + errorMessages);
//            processRepository.save(process); // Lưu trạng thái FAILED
//            statusEmitterService.emitStatusUpdate(process); // Phát tín hiệu cập nhật trạng thái
//
//            // Gửi thông báo lỗi chi tiết qua Kafka (cho giáo viên)
//            publishErrorNotification(process.getTeacherEmail(), "Quiz Creation Failed (Questions)", process.getErrorDetails()); // teacherEmail từ process entity
//
//            // Trả về phản hồi lỗi cho bước này
//            return new ProcessStepResponse(processId, "FAILED", userMessage, validationResult.getErrors());
//
//        } else {
//            // Xác thực thành công
//            System.out.println("Questions validated successfully for process " + processId + ". Proceed to import config.");
//
//            process.setStatus(QuizCreationProcess.Status.QUESTIONS_VALIDATED);
//            // Lưu dữ liệu câu hỏi đã validate vào process (chuyển DTO -> JSON String)
//            try {
//                process.setValidatedQuestionsDataJson(objectMapper.writeValueAsString(questions));
//            } catch (IOException e) {
//                String errorMessage = "Failed to serialize validated questions data: " + e.getMessage();
//                System.err.println(errorMessage);
//                process.setStatus(QuizCreationProcess.Status.FAILED);
//                process.setErrorDetails(errorMessage);
//                processRepository.save(process);
//                statusEmitterService.emitStatusUpdate(process);
//                // Log lỗi này và trả về lỗi FAILED cho bước này dù validate thành công
//                publishErrorNotification(process.getTeacherEmail(), "Quiz Creation Failed (Internal Error)", errorMessage);
//                return new ProcessStepResponse(processId, "FAILED", "Internal error saving validated data.", List.of(errorMessage));
//            }
//
//
//            processRepository.save(process); // Lưu trạng thái QUESTIONS_VALIDATED và dữ liệu câu hỏi
//            statusEmitterService.emitStatusUpdate(process); // Phát tín hiệu cập nhật trạng thái
//
//            // Trả về phản hồi thành công cho bước này
//            return new ProcessStepResponse(processId, "SUCCESS", "Questions validated successfully. Please import quiz configuration.", null);
//        }
        return null;
    }
}
