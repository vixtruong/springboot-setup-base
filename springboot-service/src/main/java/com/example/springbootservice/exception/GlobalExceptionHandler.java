package com.example.springbootservice.exception;

import com.example.springbootservice.core.AppConstants;
import com.example.springbootservice.core.AppException;
import com.example.springbootservice.core.response.ErrorResponse;
import com.example.springbootservice.core.response.OkResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Optional;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ErrorResponse> handlingRuntimeException(AppException exception) {
        int statusCode = AppConstants.ExceptionType.CODE_MAP.getOrDefault(
                exception.getType(), 500);

        ErrorResponse response = new ErrorResponse(exception.getType(), exception.getMessage(), statusCode);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
     ResponseEntity<ErrorResponse> handlingValidation(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("Validation error");

        String type = AppConstants.ExceptionType.VALIDATION_ERROR;
        int statusCode = AppConstants.ExceptionType.CODE_MAP.getOrDefault(
                type, 400);

        ErrorResponse response = new ErrorResponse(type, message, statusCode);
        return ResponseEntity.status(statusCode).body(response);
    }
}
