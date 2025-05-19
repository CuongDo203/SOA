package com.microservice.quiz_creation_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_PROCESS_STATE_EXCEPTION(1001, "Invalid process state", HttpStatus.BAD_REQUEST),
    RESOURCE_NOT_FOUND_EXCEPTION(1002, "Resource not found", HttpStatus.NOT_FOUND),
    VALIDATION_ERROR(1003, "Validation error", HttpStatus.BAD_REQUEST)
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
