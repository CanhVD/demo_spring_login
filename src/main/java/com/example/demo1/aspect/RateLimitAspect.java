package com.example.demo1.aspect;

import com.example.demo1.anotation.RateLimit;
import com.google.common.util.concurrent.RateLimiter;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class RateLimitAspect {

    private final ConcurrentHashMap<String, RateLimiter> limiters = new ConcurrentHashMap<>();

    // Tiêm HttpServletRequest vào để lấy IP của client
    private final HttpServletRequest request;

    public RateLimitAspect(HttpServletRequest request) {
        this.request = request;
    }

    @Around("@annotation(rateLimit)")
    public Object rateLimitCheck(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {
        // Lấy IP của client
        String clientIp = request.getRemoteAddr();
        String methodName = joinPoint.getSignature().toShortString();

        // Tạo key để lưu trữ RateLimiter theo IP và phương thức
        String rateLimitKey = clientIp + ":" + methodName;

        // Cấu hình RateLimiter cho từng IP và phương thức
        limiters.putIfAbsent(rateLimitKey, RateLimiter.create((double) rateLimit.requests() / rateLimit.time()));

        RateLimiter limiter = limiters.get(rateLimitKey);

        if (!limiter.tryAcquire(0, TimeUnit.SECONDS)) {
            throw new RuntimeException("Too many requests! Please wait " + rateLimit.time() + " seconds.");
        }

        return joinPoint.proceed();
    }
}
