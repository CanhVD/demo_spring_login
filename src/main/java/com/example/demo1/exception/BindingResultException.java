package com.example.demo1.exception;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

public class BindingResultException extends RuntimeException {

    public BindingResultException(String message) {
        super(message);
    }

    public BindingResultException(BindingResult bindingResult) {
        List<String> errorMessages = bindingResult.getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();
        throw new BindingResultException(errorMessages.toString());
    }
}
