package com.example.springbootservice.core.enums;

public enum ErrorCode {
    VALIDATION_ERROR(400),
    ENTITY_NOT_FOUND(404),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    INTERNAL_ERROR(500),
    BAD_REQUEST(400),
    DUPLICATE_RESOURCE(409),
    CONFLICT(409),
    TIMEOUT(408),
    SERVICE_UNAVAILABLE(503);

    private final int code;

    ErrorCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static ErrorCode fromString(String name) {
        for (ErrorCode type : ErrorCode.values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown exception type: " + name);
    }
}
