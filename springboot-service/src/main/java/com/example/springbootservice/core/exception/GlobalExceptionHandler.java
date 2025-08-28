package com.example.springbootservice.core.exception;

import com.example.springbootservice.core.enums.ErrorCode;
import com.example.springbootservice.core.response.ErrorResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ErrorResponse> handlingRuntimeException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();

        ErrorResponse response = ErrorResponse.builder()
                .code(errorCode.getCode())
                .type(errorCode.name())
                .message(exception.getMessage())
                .build();
        return ResponseEntity.status(errorCode.getCode()).body(response);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResponse> handlingValidation(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("Validation error");

        ErrorCode errorCode = ErrorCode.VALIDATION_ERROR;

        ErrorResponse response = ErrorResponse.builder()
                .code(errorCode.getCode())
                .type(errorCode.name())
                .message(message)
                .build();

        return ResponseEntity.status(errorCode.getCode()).body(response);
    }
}
