package com.example.validation_service.dto;
import java.util.List;

public class StudentListRequest {
    private List<String> studentCodes;
    private List<String> studentNames;
    public List<String> getStudentCodeList(){
        return this.studentCodes;
    }

    public List<String> getStudentNameList(){
        return this.studentNames;
    }
}
