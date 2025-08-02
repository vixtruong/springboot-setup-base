package com.example.springbootservice.exception;

import com.example.springbootservice.core.AppException;
import com.example.springbootservice.core.AppResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Optional;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<AppResponse<String>> handlingRuntimeException(AppException exception) {
        AppResponse<String> response = AppResponse.failure(
                String.format("[%s] %s", exception.getType(), exception.getMessage()));

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<AppResponse<String>> handlingValidation(MethodArgumentNotValidException exception) {
        String message = Optional.ofNullable(exception.getFieldError())
                .map(FieldError::getDefaultMessage)
                .orElse("Validation failed");

        AppResponse<String> response = AppResponse.failure(message);

        return ResponseEntity.badRequest().body(response);
    }
}
