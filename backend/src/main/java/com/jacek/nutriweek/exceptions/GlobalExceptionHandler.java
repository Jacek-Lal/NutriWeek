package com.jacek.nutriweek.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = UserAlreadyExistsException.class)
    public ResponseEntity<CustomErrorResponse> handleUserExistsException(UserAlreadyExistsException e){
        HttpStatus status = HttpStatus.CONFLICT;
        CustomErrorResponse response = new CustomErrorResponse(status.value(), e.getMessage());
        return ResponseEntity
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
