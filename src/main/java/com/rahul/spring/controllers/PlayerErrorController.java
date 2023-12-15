package com.rahul.spring.controllers;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class PlayerErrorController {
    @ExceptionHandler
    ResponseEntity handleJPAViolation(TransactionSystemException exception){

        ResponseEntity.BodyBuilder resposneEntity = ResponseEntity.badRequest();

        if(exception.getCause().getCause() instanceof ConstraintViolationException){
            ConstraintViolationException ex = (ConstraintViolationException) exception.getCause().getCause();
            List error = ex.getConstraintViolations().stream()
                    .map(constraintViolation -> {
                       Map<String, String> err = new HashMap<>();
                       err.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
                       return err;
                    }).collect(Collectors.toList());
            return resposneEntity.body(error);
        }
        return resposneEntity.build();
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity handleException(MethodArgumentNotValidException e){
        List errorList = e.getFieldErrors().stream()
                .map(fieldError -> {
                    Map<String, String> error = new HashMap<>();
                    error.put(fieldError.getField(), fieldError.getDefaultMessage() );
                    return error;
                }).collect(Collectors.toList());
        return ResponseEntity.badRequest().body(errorList);
    }
}
