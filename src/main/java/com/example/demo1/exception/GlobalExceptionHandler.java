package com.example.demo1.exception;

import com.example.demo1.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice // Chỉ định lớp này xử lý ngoại lệ chung
@Slf4j
public class GlobalExceptionHandler<T> {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<T>> handleGeneralException(Exception exception) {
        log.error("Exception cause = {}", exception.getMessage());
        BaseResponse<T> response = BaseResponse.<T>builder().code(1).msg(exception.getMessage()).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(BindingResultException.class)
    public ResponseEntity<?> handleResourceNotFoundException(BindingResultException exception) {
        log.error("Exception cause = {}", exception.getMessage());
        BaseResponse<T>  response = BaseResponse.<T>builder().code(2).msg(exception.getMessage()).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
