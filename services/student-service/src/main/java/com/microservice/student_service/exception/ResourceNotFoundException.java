package com.microservice.student_service.exception;

public class ResourceNotFoundException extends RuntimeException{

    private ErrorCode errorCode;

    public ResourceNotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
