package com.example.springbootservice.core.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "success", "code", "type", "message" })
public class ErrorResponse implements ApiResponse {
    private final boolean success = false;
    private final int code;
    private final String type;
    private final String message;

    public ErrorResponse(String type, String message, int code) {
        this.type = type;
        this.code = code;
        this.message = message;
    }

    @Override
    public boolean isSuccess() { return success; }

    @Override
    public int getCode() { return code; }

    public String getType() { return type; }

    @Override
    public String getMessage() { return message; }
}

