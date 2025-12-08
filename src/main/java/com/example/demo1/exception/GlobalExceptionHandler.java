package com.example.demo1.exception;

import com.example.demo1.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice // Chỉ định lớp này xử lý ngoại lệ chung
@Slf4j
public class GlobalExceptionHandler<T> {

    @ResponseBody
    @ExceptionHandler(BindingResultException.class)
    public BaseResponse<T> handleResourceNotFoundException(BindingResultException exception) {
        log.error("Exception cause = {}", exception.getMessage());
        BaseResponse<T>  response = BaseResponse.<T>builder().code(2).msg(exception.getMessage()).build();
        return response;
    }

    @ResponseBody
    //@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public BaseResponse<T> handleGeneralException(Exception exception) {
        log.error("Exception cause = {}", exception.getMessage());
        return BaseResponse.<T>builder().code(1).msg(exception.getMessage()).build();
    }
}
