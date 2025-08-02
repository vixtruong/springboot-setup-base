package com.example.springbootservice.core;

public class AppException extends RuntimeException {

    private final String type;

    public AppException(String type, String message) {
        super(message);
        this.type = type;
    }

    public AppException(String type, String message, Throwable cause) {
        super(message, cause);
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
