package com.example.springbootservice.core.enums;

public enum SuccessCode {
    OK(200),
    CREATED(201),
    ACCEPTED(202),
    NO_CONTENT(204);

    private final int code;

    SuccessCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

