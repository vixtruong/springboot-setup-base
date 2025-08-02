package com.example.springbootservice.core;

public class AppResponse<T> {
    private boolean success;
    private String message;
    private T data;

    public AppResponse() {}

    public AppResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }

    public static <T> AppResponse<T> success(String message, T data) {
        return new AppResponse<>(true, message, data);
    }

    public static <T> AppResponse<T> failure(String message) {
        return new AppResponse<>(false, message, null);
    }
}
