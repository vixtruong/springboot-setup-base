package com.example.springbootservice.core.response;

public interface ApiResponse {
    boolean isSuccess();
    int getCode();
    String getMessage();
}
