package com.example.demo1.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PageResponse<T> {
    private long total;
    private T result;
}
