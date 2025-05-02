package com.microservice.student_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public enum ErrorCode {
        UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
        STUDENT_EXISTED(1001, "Student already existed", HttpStatus.CONFLICT),
        INVALID_MESSAGE_KEY(1002, "Invalid message key", HttpStatus.BAD_REQUEST),
        STUDENT_NOT_EXISTED(1003, "Student not existed",HttpStatus.NOT_FOUND),
        STUDENT_CODE_REQUIRED(1004, "Student code is required", HttpStatus.BAD_REQUEST),
        FIRST_NAME_REQUIRED(1005, "First name is required", HttpStatus.BAD_REQUEST),
        LAST_NAME_REQUIRED(1006, "Last name is required", HttpStatus.BAD_REQUEST),
        EMAIL_NOT_VALID(1007, "Email should be valid", HttpStatus.BAD_REQUEST),
        STUDENT_NOT_FOUND(1008, "Student not found", HttpStatus.NOT_FOUND)
    ;

    private int code;
    private String message;
    private HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatusCode getStatusCode() {
        return statusCode;
    }
}
