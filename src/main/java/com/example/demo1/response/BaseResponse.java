package com.example.demo1.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BaseResponse<T> {

    private Integer code;

    private String msg;

    private T data;
}
