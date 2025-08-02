package com.example.springbootservice.core.response;

import com.example.springbootservice.core.AppConstants;

public class OkResponse<T> implements ApiResponse {
    private final boolean success = true;
    private final int code;
    private final String message;
    private final T data;

    public OkResponse(String message, T data) {
        this.code = AppConstants.SuccessCode.OK;
        this.message = message;
        this.data = data;
    }

    public OkResponse(T data) {
        this.code = AppConstants.SuccessCode.OK;
        this.message = null;
        this.data = data;
    }

    public OkResponse(String message, T data, int code) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    @Override
    public boolean isSuccess() { return success; }

    @Override
    public int getCode() { return code; }

    @Override
    public String getMessage() { return message; }

    public T getData() { return data; }
}

