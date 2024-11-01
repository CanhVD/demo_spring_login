package com.example.demo1.controller;

import com.example.demo1.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.function.Supplier;

public class BaseRestController {

    protected <T> ResponseEntity<BaseResponse<T>> execute(Supplier<T> supplier) {
        try {
            T result = supplier.get();
            return successResponse(result);
        } catch (Exception ex) {
            return errorResponse(ex);
        }
    }

    protected <T> ResponseEntity<BaseResponse<T>> successResponse(T data) {
        return new ResponseEntity<>(
                BaseResponse.<T>builder().code(0).msg("Success").data(data).build(),
                HttpStatus.OK
        );
    }

    protected <T> ResponseEntity<BaseResponse<T>> errorResponse(Exception exception) {
        BaseResponse<T> response = BaseResponse.<T>builder().code(1).msg(exception.getMessage()).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
