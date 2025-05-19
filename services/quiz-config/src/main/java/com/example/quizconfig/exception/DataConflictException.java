package com.example.quizconfig.exception;

public class DataConflictException extends RuntimeException {

    private ErrorCode errorCode;

    public DataConflictException(ErrorCode errorCode) {
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