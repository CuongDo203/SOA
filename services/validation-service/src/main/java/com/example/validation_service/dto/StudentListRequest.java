package com.example.validation_service.dto;
import java.util.List;

public class StudentListRequest {
    private List<StudentDto> studentCodes;
    public List<StudentDto> getStudentCodeList(){
        return this.studentCodes;
    }
}
