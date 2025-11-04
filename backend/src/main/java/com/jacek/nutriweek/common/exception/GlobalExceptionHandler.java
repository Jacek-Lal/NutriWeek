package com.jacek.nutriweek.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = UserAlreadyExistsException.class)
    public ResponseEntity<CustomErrorResponse> handleUserExistsException(UserAlreadyExistsException e){
        HttpStatus status = HttpStatus.CONFLICT;
        return createResponse(status, e);
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleResourceNotFound(Exception e){
        HttpStatus status = HttpStatus.NOT_FOUND;
        return createResponse(status, e);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<CustomErrorResponse> handleInternalError(Exception e){
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return createResponse(status, e);
    }

    private ResponseEntity<CustomErrorResponse> createResponse(HttpStatus status, Exception e){
        CustomErrorResponse response = new CustomErrorResponse(status.value(), e.getMessage(), Instant.now());
        return ResponseEntity.status(status).body(response);
    }
}
