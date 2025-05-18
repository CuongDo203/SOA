package com.example.importservice.service;

import com.example.importservice.dto.QuestionDTO;
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
public class ImportServiceImpl { // Đổi tên nếu bạn muốn có interface ImportService

    private static final Logger log = LoggerFactory.getLogger(ImportServiceImpl.class);

    // Điều chỉnh các hằng số này cho phù hợp với cấu trúc file Excel của bạn
    private static final int COL_CONTENT = 0;
    private static final int COL_ANS_A = 1;
    private static final int COL_ANS_B = 2;
    private static final int COL_ANS_C = 3;
    private static final int COL_ANS_D = 4;
    private static final int COL_CORRECT_ANS = 5;
    private static final int COL_SUBJECT_ID = 6;
    private static final int COL_LEVEL_ID = 7;

    /**
     * Xử lý một file Excel chứa danh sách câu hỏi và chuyển đổi thành List<QuestionDTO>.
     *
     * @param excelFile File Excel được upload.
     * @return Danh sách QuestionDTO được parse từ file.
     * @throws IOException Nếu có lỗi khi đọc file.
     * @throws IllegalArgumentException Nếu file trống hoặc không hợp lệ.
     */
    public List<QuestionDTO> parseQuestionsFromExcel(MultipartFile excelFile) throws IOException {
        if (excelFile == null || excelFile.isEmpty()) {
            log.warn("Excel file is null or empty.");
            throw new IllegalArgumentException("Uploaded Excel file cannot be null or empty.");
        }

        List<QuestionDTO> questions = new ArrayList<>();
        List<String> parsingErrors = new ArrayList<>(); // Để thu thập lỗi parsing

        log.info("Processing Excel file for questions: {}", excelFile.getOriginalFilename());

        try (InputStream inputStream = excelFile.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(inputStream); // Tự động nhận dạng .xls hoặc .xlsx
            // Giả sử chỉ đọc sheet đầu tiên
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                log.error("No sheet found in the Excel file: {}", excelFile.getOriginalFilename());
                throw new IllegalArgumentException("Excel file does not contain any sheets.");
            }

            Iterator<Row> rowIterator = sheet.iterator();

            // Bỏ qua dòng tiêu đề (nếu có)
            if (rowIterator.hasNext()) {
                rowIterator.next(); // Bỏ qua dòng đầu tiên
                log.debug("Skipped header row in file: {}", excelFile.getOriginalFilename());
            } else {
                log.warn("Excel file is empty (no header row or data): {}", excelFile.getOriginalFilename());
                return questions; // Trả về danh sách rỗng nếu không có cả header
            }

            int rowNumForLog = 1; // Để log số dòng chính xác (bắt đầu từ 1 sau header)
            while (rowIterator.hasNext()) {
                Row currentRow = rowIterator.next();
                rowNumForLog++;
                try {
                    QuestionDTO questionDTO = parseRowToQuestionDTO(currentRow, excelFile.getOriginalFilename(), rowNumForLog);
                    if (questionDTO != null) { // Chỉ thêm nếu parse thành công và không phải dòng trống
                        questions.add(questionDTO);
                    }
                } catch (Exception e) { // Bắt các lỗi cụ thể hơn nếu cần
                    String errorMsg = String.format("Error parsing row %d in file '%s': %s",
                            rowNumForLog, excelFile.getOriginalFilename(), e.getMessage());
                    log.error(errorMsg, e);
                    parsingErrors.add(errorMsg);
                    // Tùy chọn: có thể ném exception ở đây nếu một lỗi parsing là nghiêm trọng
                    // throw new RuntimeException(errorMsg, e);
                }
            }
            workbook.close();
        } catch (IOException e) {
            log.error("IOException while processing file '{}': {}", excelFile.getOriginalFilename(), e.getMessage(), e);
            throw e; // Ném lại IOException để controller xử lý
        } catch (Exception e) { // Bắt các lỗi khác từ POI như InvalidFormatException, EncryptedDocumentException
            log.error("Error processing Excel file '{}': {}", excelFile.getOriginalFilename(), e.getMessage(), e);
            throw new IOException("Failed to process Excel file: " + e.getMessage(), e); // Bọc lại thành IOException
        }

        if (!parsingErrors.isEmpty()) {
            // Quyết định cách xử lý:
            // 1. Ném một exception tổng hợp các lỗi
            // throw new ExcelParsingException("Encountered errors during Excel parsing.", parsingErrors);
            // 2. Hoặc chỉ log và trả về những gì đã parse được (như hiện tại)
            log.warn("Encountered {} parsing errors while processing file '{}'. See previous logs for details.",
                    parsingErrors.size(), excelFile.getOriginalFilename());
        }

        log.info("Successfully parsed {} questions from file: {}", questions.size(), excelFile.getOriginalFilename());
        return questions;
    }

    private QuestionDTO parseRowToQuestionDTO(Row row, String filename, int rowNumForLog) {
        if (isRowEffectivelyEmpty(row)) {
            log.debug("Skipping empty row {} in file {}", rowNumForLog, filename);
            return null;
        }

        QuestionDTO questionDTO = new QuestionDTO();

        questionDTO.setContent(getCellValueAsString(row.getCell(COL_CONTENT), filename, rowNumForLog, "Content"));
        questionDTO.setAnsA(getCellValueAsString(row.getCell(COL_ANS_A), filename, rowNumForLog, "Answer A"));
        questionDTO.setAnsB(getCellValueAsString(row.getCell(COL_ANS_B), filename, rowNumForLog, "Answer B"));
        questionDTO.setAnsC(getCellValueAsString(row.getCell(COL_ANS_C), filename, rowNumForLog, "Answer C"));
        questionDTO.setAnsD(getCellValueAsString(row.getCell(COL_ANS_D), filename, rowNumForLog, "Answer D"));
        questionDTO.setCorrectAns(getCellValueAsString(row.getCell(COL_CORRECT_ANS), filename, rowNumForLog, "Correct Answer"));

        questionDTO.setSubjectId(getCellValueAsLong(row.getCell(COL_SUBJECT_ID), filename, rowNumForLog, "Subject ID"));
        questionDTO.setLevelId(getCellValueAsLong(row.getCell(COL_LEVEL_ID), filename, rowNumForLog, "Level ID"));

        // Validate DTO cơ bản (ví dụ: content không được rỗng)
        if (questionDTO.getContent() == null || questionDTO.getContent().trim().isEmpty()) {
            log.warn("Question content is empty at row {} in file {}. Skipping this question.", rowNumForLog, filename);
            // Có thể ném một lỗi cụ thể hơn ở đây nếu content là bắt buộc
            // throw new IllegalArgumentException("Question content cannot be empty at row " + rowNumForLog);
            return null;
        }
        // Thêm các bước validation khác nếu cần

        return questionDTO;
    }

    private String getCellValueAsString(Cell cell, String filename, int rowNum, String columnName) {
        if (cell == null) {
            log.trace("Cell for {} at row {} in file {} is null.", columnName, rowNum, filename);
            return null;
        }
        DataFormatter formatter = new DataFormatter();
        String value = formatter.formatCellValue(cell).trim();
        if (value.isEmpty()) {
            log.trace("Cell for {} at row {} in file {} is empty after trim.", columnName, rowNum, filename);
            return null; // Trả về null nếu sau khi trim thì rỗng, thay vì chuỗi rỗng ""
        }
        return value;
    }

    private Long getCellValueAsLong(Cell cell, String filename, int rowNum, String columnName) {
        String cellValue = getCellValueAsString(cell, filename, rowNum, columnName);
        if (cellValue == null) { // Đã xử lý isEmpty trong getCellValueAsString
            return null;
        }
        try {
            if (cellValue.contains(".")) { // Xử lý số như "123.0"
                cellValue = cellValue.substring(0, cellValue.indexOf('.'));
            }
            return Long.parseLong(cellValue);
        } catch (NumberFormatException e) {
            log.warn("Cannot parse '{}' as Long for {} at row {} in file {}. Error: {}",
                    cellValue, columnName, rowNum, filename, e.getMessage());
            // Tùy chọn: Ném exception nếu giá trị này là bắt buộc và không hợp lệ
            // throw new IllegalArgumentException("Invalid number format for " + columnName + " at row " + rowNum + ": " + cellValue);
            return null;
        }
    }

    private boolean isRowEffectivelyEmpty(Row row) {
        if (row == null) {
            return true;
        }
        // Kiểm tra một vài ô quan trọng, nếu tất cả đều trống thì coi là dòng trống
        // Ví dụ: kiểm tra ô content, nếu trống thì coi là dòng trống
        Cell firstCell = row.getCell(COL_CONTENT); // Giả sử cột content là cột đầu tiên quan trọng
        String firstCellValue = getCellValueAsString(firstCell, "", row.getRowNum() + 1, "Content (for empty check)");
        return firstCellValue == null; // Hoặc firstCellValue.trim().isEmpty() nếu bạn cho phép chuỗi chỉ có khoảng trắng
    }
}