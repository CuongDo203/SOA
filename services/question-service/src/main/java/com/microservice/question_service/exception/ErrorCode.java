package com.microservice.question_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    OPTION_A_NOT_NULL(1001, "Option A must not be null", HttpStatus.BAD_REQUEST),
    OPTION_B_NOT_NULL(1002, "Option B must not be null", HttpStatus.BAD_REQUEST),
    OPTION_C_NOT_NULL(1003, "Option C must not be null", HttpStatus.BAD_REQUEST),
    OPTION_D_NOT_NULL(1004, "Option D must not be null", HttpStatus.BAD_REQUEST),
    ANSWER_NOT_NULL(1005, "Answer must not be null", HttpStatus.BAD_REQUEST),
    QUESTION_NOT_NULL(1006, "Question must not be null", HttpStatus.BAD_REQUEST),
    QUESTION_NOT_FOUND(1007, "Question not found", HttpStatus.NOT_FOUND),
    INVALID_MESSAGE_KEY(1008, "Invalid message key", HttpStatus.BAD_REQUEST),
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
