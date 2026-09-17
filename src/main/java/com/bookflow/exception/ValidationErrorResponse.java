package com.bookflow.exception;

import java.time.LocalDateTime;
import java.util.Map;

public class ValidationErrorResponse {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private Map<String, String> errors;
    private String path;

    public ValidationErrorResponse(
            LocalDateTime timestamp,
            int status,
            String error,
            Map<String, String> errors,
            String path) {

        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.errors = errors;
        this.path = path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public String getPath() {
        return path;
    }
}