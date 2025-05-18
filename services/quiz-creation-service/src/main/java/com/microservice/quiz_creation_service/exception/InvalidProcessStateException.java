package com.microservice.quiz_creation_service.exception;

public class InvalidProcessStateException extends RuntimeException {
    private ErrorCode errorCode;
    public InvalidProcessStateException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    public ErrorCode getErrorCode() {
        return errorCode;
    }
    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
