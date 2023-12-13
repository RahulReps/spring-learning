package com.rahul.spring.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class PlayerErrorController {
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
