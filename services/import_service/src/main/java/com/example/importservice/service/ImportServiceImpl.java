package com.example.importservice.service;

import com.example.importservice.dto.QuestionParsedResponse;
import com.example.importservice.dto.StudentParsedResponse; // THÊM IMPORT NÀY
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ImportServiceImpl {

    private static final Logger log = LoggerFactory.getLogger(ImportServiceImpl.class);

    // --- Constants cho Question Excel ---
    private static final int Q_COL_CONTENT = 0;
    private static final int Q_COL_ANS_A = 1;
    private static final int Q_COL_ANS_B = 2;
    private static final int Q_COL_ANS_C = 3;
    private static final int Q_COL_ANS_D = 4;
    private static final int Q_COL_CORRECT_ANS = 5;
    // private static final int Q_COL_SUBJECT_ID = 6;
    // private static final int Q_COL_LEVEL_ID = 7;

    // --- Constants cho Student Excel (BẠN CẦN ĐỊNH NGHĨA CHO ĐÚNG FILE EXCEL SINH VIÊN) ---
    private static final int S_COL_STUDENT_CODE = 0;
    private static final int S_COL_FIRST_NAME = 1;
    private static final int S_COL_LAST_NAME = 2;
    private static final int S_COL_EMAIL = 3;
    private static final int S_COL_CLASS_NAME = 4;
    // Thêm các cột khác nếu cần


    public List<QuestionParsedResponse> parseQuestionsFromExcel(MultipartFile excelFile) throws IOException {
        // ... (code parseQuestionsFromExcel của bạn giữ nguyên) ...
        // Chỉ đổi tên hằng số cột để rõ ràng hơn (Q_COL_*)
        if (excelFile == null || excelFile.isEmpty()) {
            log.warn("Excel file (questions) is null or empty.");
            throw new IllegalArgumentException("Uploaded Excel file for questions cannot be null or empty.");
        }

        List<QuestionParsedResponse> questions = new ArrayList<>();
        List<String> parsingErrors = new ArrayList<>();

        log.info("Processing Excel file for questions: {}", excelFile.getOriginalFilename());

        try (InputStream inputStream = excelFile.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                log.error("No sheet found in the Excel file (questions): {}", excelFile.getOriginalFilename());
                throw new IllegalArgumentException("Excel file for questions does not contain any sheets.");
            }

            Iterator<Row> rowIterator = sheet.iterator();

            if (rowIterator.hasNext()) {
                rowIterator.next(); // Skip header
                log.debug("Skipped header row in question file: {}", excelFile.getOriginalFilename());
            } else {
                log.warn("Question Excel file is empty: {}", excelFile.getOriginalFilename());
                return questions;
            }

            int rowNumForLog = 1;
            while (rowIterator.hasNext()) {
                Row currentRow = rowIterator.next();
                rowNumForLog++;
                try {
                    QuestionParsedResponse questionParsedResponse = parseRowToQuestionDTO(currentRow, excelFile.getOriginalFilename(), rowNumForLog);
                    if (questionParsedResponse != null) {
                        questions.add(questionParsedResponse);
                    }
                } catch (Exception e) {
                    String errorMsg = String.format("Error parsing question row %d in file '%s': %s",
                            rowNumForLog, excelFile.getOriginalFilename(), e.getMessage());
                    log.error(errorMsg, e);
                    parsingErrors.add(errorMsg);
                }
            }
            workbook.close();
        } catch (IOException e) {
            log.error("IOException while processing question file '{}': {}", excelFile.getOriginalFilename(), e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Error processing question Excel file '{}': {}", excelFile.getOriginalFilename(), e.getMessage(), e);
            throw new IOException("Failed to process question Excel file: " + e.getMessage(), e);
        }

        if (!parsingErrors.isEmpty()) {
            log.warn("Encountered {} parsing errors while processing question file '{}'.",
                    parsingErrors.size(), excelFile.getOriginalFilename());
        }
        log.info("Successfully parsed {} questions from file: {}", questions.size(), excelFile.getOriginalFilename());
        return questions;
    }

    private QuestionParsedResponse parseRowToQuestionDTO(Row row, String filename, int rowNumForLog) {
        // Đổi tên hằng số cột để rõ ràng hơn (Q_COL_*)
        if (isRowEffectivelyEmpty(row, Q_COL_CONTENT)) { // Kiểm tra dựa trên cột content của Question
            log.debug("Skipping empty question row {} in file {}", rowNumForLog, filename);
            return null;
        }
        QuestionParsedResponse questionParsedResponse = new QuestionParsedResponse();
        questionParsedResponse.setContent(getCellValueAsString(row.getCell(Q_COL_CONTENT), filename, rowNumForLog, "Q_Content"));
        questionParsedResponse.setOptionA(getCellValueAsString(row.getCell(Q_COL_ANS_A), filename, rowNumForLog, "Q_AnsA"));
        questionParsedResponse.setOptionB(getCellValueAsString(row.getCell(Q_COL_ANS_B), filename, rowNumForLog, "Q_AnsB"));
        questionParsedResponse.setOptionC(getCellValueAsString(row.getCell(Q_COL_ANS_C), filename, rowNumForLog, "Q_AnsC"));
        questionParsedResponse.setOptionD(getCellValueAsString(row.getCell(Q_COL_ANS_D), filename, rowNumForLog, "Q_AnsD"));
        questionParsedResponse.setAnswerKey(getCellValueAsString(row.getCell(Q_COL_CORRECT_ANS), filename, rowNumForLog, "Q_CorrectAns"));
        // questionParsedResponse.setSubjectId(getCellValueAsLong(row.getCell(Q_COL_SUBJECT_ID), filename, rowNumForLog, "Q_SubjectId"));
        // questionParsedResponse.setLevelId(getCellValueAsLong(row.getCell(Q_COL_LEVEL_ID), filename, rowNumForLog, "Q_LevelId"));
        if (questionParsedResponse.getContent() == null || questionParsedResponse.getContent().trim().isEmpty()) {
            log.warn("Question content is empty at row {} in file {}. Skipping this question.", rowNumForLog, filename);
            return null;
        }
        return questionParsedResponse;
    }


    // --- PHƯƠNG THỨC MỚI ĐỂ PARSE STUDENT EXCEL ---
    public List<StudentParsedResponse> parseStudentsFromExcel(MultipartFile excelFile) throws IOException {
        if (excelFile == null || excelFile.isEmpty()) {
            log.warn("Excel file (students) is null or empty.");
            throw new IllegalArgumentException("Uploaded Excel file for students cannot be null or empty.");
        }

        List<StudentParsedResponse> students = new ArrayList<>();
        List<String> parsingErrors = new ArrayList<>();

        log.info("Processing Excel file for students: {}", excelFile.getOriginalFilename());

        try (InputStream inputStream = excelFile.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0); // Giả sử sheet đầu tiên
            if (sheet == null) {
                log.error("No sheet found in the Excel file (students): {}", excelFile.getOriginalFilename());
                throw new IllegalArgumentException("Excel file for students does not contain any sheets.");
            }

            Iterator<Row> rowIterator = sheet.iterator();

            // Bỏ qua dòng tiêu đề
            if (rowIterator.hasNext()) {
                rowIterator.next();
                log.debug("Skipped header row in student file: {}", excelFile.getOriginalFilename());
            } else {
                log.warn("Student Excel file is empty: {}", excelFile.getOriginalFilename());
                return students;
            }

            int rowNumForLog = 1;
            while (rowIterator.hasNext()) {
                Row currentRow = rowIterator.next();
                rowNumForLog++;
                try {
                    StudentParsedResponse studentParsedResponse = parseRowToStudentDTO(currentRow, excelFile.getOriginalFilename(), rowNumForLog);
                    if (studentParsedResponse != null) {
                        students.add(studentParsedResponse);
                    }
                } catch (Exception e) {
                    String errorMsg = String.format("Error parsing student row %d in file '%s': %s",
                            rowNumForLog, excelFile.getOriginalFilename(), e.getMessage());
                    log.error(errorMsg, e);
                    parsingErrors.add(errorMsg);
                }
            }
            workbook.close();
        } catch (IOException e) {
            log.error("IOException while processing student file '{}': {}", excelFile.getOriginalFilename(), e.getMessage(), e);
            throw e;
        } catch (Exception e) { // Bắt các lỗi khác từ POI
            log.error("Error processing student Excel file '{}': {}", excelFile.getOriginalFilename(), e.getMessage(), e);
            throw new IOException("Failed to process student Excel file: " + e.getMessage(), e);
        }

        if (!parsingErrors.isEmpty()) {
            log.warn("Encountered {} parsing errors while processing student file '{}'.",
                    parsingErrors.size(), excelFile.getOriginalFilename());
        }

        log.info("Successfully parsed {} students from file: {}", students.size(), excelFile.getOriginalFilename());
        return students;
    }

    private StudentParsedResponse parseRowToStudentDTO(Row row, String filename, int rowNumForLog) {
        // Kiểm tra dòng trống dựa trên cột studentCode (ví dụ)
        if (isRowEffectivelyEmpty(row, S_COL_STUDENT_CODE)) {
            log.debug("Skipping empty student row {} in file {}", rowNumForLog, filename);
            return null;
        }

        StudentParsedResponse studentParsedResponse = new StudentParsedResponse(); // Sử dụng constructor mặc định
        // Hoặc dùng builder nếu bạn đã thêm @Builder vào StudentParsedResponse

        studentParsedResponse.setStudentCode(getCellValueAsString(row.getCell(S_COL_STUDENT_CODE), filename, rowNumForLog, "S_StudentCode"));
        studentParsedResponse.setFirstName(getCellValueAsString(row.getCell(S_COL_FIRST_NAME), filename, rowNumForLog, "S_FirstName"));
        studentParsedResponse.setLastName(getCellValueAsString(row.getCell(S_COL_LAST_NAME), filename, rowNumForLog, "S_LastName"));
        studentParsedResponse.setEmail(getCellValueAsString(row.getCell(S_COL_EMAIL), filename, rowNumForLog, "S_Email"));
        studentParsedResponse.setClassName(getCellValueAsString(row.getCell(S_COL_CLASS_NAME), filename, rowNumForLog, "S_ClassName"));

        // Thêm validation cơ bản cho StudentParsedResponse (ví dụ: studentCode không được rỗng)
        // Bạn đã có @NotBlank trên StudentParsedResponse, nhưng kiểm tra ở đây có thể cho log cụ thể hơn về dòng lỗi.
        if (studentParsedResponse.getStudentCode() == null || studentParsedResponse.getStudentCode().trim().isEmpty()) {
            log.warn("Student code is empty at row {} in file {}. Skipping this student.", rowNumForLog, filename);
            return null; // Hoặc ném lỗi tùy theo yêu cầu
        }
        // Thêm các validation khác nếu cần (ví dụ: định dạng email,...)
        // Mặc dù @Email đã có trên DTO, việc kiểm tra ở đây có thể bắt lỗi sớm hơn
        // hoặc cho phép xử lý tùy chỉnh.

        return studentParsedResponse;
    }


    // --- CÁC HÀM TIỆN ÍCH (GIỮ NGUYÊN HOẶC CẬP NHẬT) ---
    private String getCellValueAsString(Cell cell, String filename, int rowNum, String columnName) {
        // ... (giữ nguyên code của bạn) ...
        if (cell == null) {
            log.trace("Cell for {} at row {} in file {} is null.", columnName, rowNum, filename);
            return null;
        }
        DataFormatter formatter = new DataFormatter();
        String value = formatter.formatCellValue(cell).trim();
        if (value.isEmpty()) {
            log.trace("Cell for {} at row {} in file {} is empty after trim.", columnName, rowNum, filename);
            return null;
        }
        return value;
    }

    private Long getCellValueAsLong(Cell cell, String filename, int rowNum, String columnName) {
        // ... (giữ nguyên code của bạn, có thể không cần cho StudentParsedResponse hiện tại) ...
        String cellValue = getCellValueAsString(cell, filename, rowNum, columnName);
        if (cellValue == null) {
            return null;
        }
        try {
            if (cellValue.contains(".")) {
                cellValue = cellValue.substring(0, cellValue.indexOf('.'));
            }
            return Long.parseLong(cellValue);
        } catch (NumberFormatException e) {
            log.warn("Cannot parse '{}' as Long for {} at row {} in file {}. Error: {}",
                    cellValue, columnName, rowNum, filename, e.getMessage());
            return null;
        }
    }

    // Cập nhật isRowEffectivelyEmpty để nhận cột kiểm tra
    private boolean isRowEffectivelyEmpty(Row row, int primaryColumnIndex) {
        if (row == null) {
            return true;
        }
        Cell primaryCell = row.getCell(primaryColumnIndex);
        String primaryCellValue = getCellValueAsString(primaryCell, "", row.getRowNum() + 1, "PrimaryCheckCol");
        return primaryCellValue == null;
    }
}