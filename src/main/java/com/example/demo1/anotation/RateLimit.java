package com.example.demo1.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)  // Chỉ áp dụng cho method
@Retention(RetentionPolicy.RUNTIME)  // Giữ lại trong runtime để AOP xử lý
public @interface RateLimit {
    int requests();  // Số request tối đa trong thời gian giới hạn
    int time();  // Thời gian giới hạn (tính bằng giây)
}
