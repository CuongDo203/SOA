package com.example.quizconfig.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    QUIZ_EXISTED(2001, "Quiz already existed", HttpStatus.CONFLICT),
    QUIZ_NOT_FOUND(2002, "Quiz not found", HttpStatus.NOT_FOUND),
    CONFIG_NOT_FOUND(2003, "Config not found", HttpStatus.NOT_FOUND),
    INVALID_INPUT(2004, "Invalid input", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

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