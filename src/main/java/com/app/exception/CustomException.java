package com.app.exception;

public class CustomException extends RuntimeException {
    private ExceptionInfo exceptionInfo;

    public CustomException(ExceptionInfo exceptionInfo) {
        this.exceptionInfo = exceptionInfo;
    }

    public ExceptionInfo getExceptionInfo() {
        return exceptionInfo;
    }
}
