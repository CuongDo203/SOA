package com.example.validation_service.dto;

import java.util.List;
public class ValidationResult {
    private boolean valid;
    private List<String> errors;

    public ValidationResult() {}

    public ValidationResult(boolean valid, List<String> errors) {
        this.valid = valid;
        this.errors = errors;
    }

    public boolean isValid() {
        return this.valid;
    }

    public List<String> getErrors(){
        return this.errors;
    }
}
