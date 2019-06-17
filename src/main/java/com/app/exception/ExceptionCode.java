package com.app.exception;

public enum ExceptionCode {
    UNIDENTIED ("UNIDENTIFIED EXCEPTION"),
    REPOSITORY ("REPOSITORY EXCEPTION"),
    SERVICE ("SERVICE EXCEPTION"),
    CONTROLLER ("CONTROLLER EXCEPTION");

    private String description;

    ExceptionCode(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
