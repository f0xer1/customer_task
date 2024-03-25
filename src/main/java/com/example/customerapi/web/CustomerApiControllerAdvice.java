package com.example.customerapi.web;


import com.example.customerapi.web.dto.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.stream.Collectors;


@RestControllerAdvice
public class CustomerApiControllerAdvice {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleBadRequest(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(exceptionResponse(exception.getMessage()));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationError(
            MethodArgumentNotValidException exception) {

        var errorMessages = exception.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .filter(Objects::nonNull)
                .sorted()
                .collect(Collectors.toList());


        return ResponseEntity.badRequest().body(new ExceptionResponse(
                String.join(", ", errorMessages),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss"))));
    }

    private ExceptionResponse exceptionResponse(String message) {
        return new ExceptionResponse(message,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss")));
    }
}
