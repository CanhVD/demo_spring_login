package com.example.demo1.controller;

import com.example.demo1.request.LoginResquest;
import com.example.demo1.request.RefreshTokenResquest;
import com.example.demo1.response.BaseResponse;
import com.example.demo1.response.AuthResponse;
import com.example.demo1.service.auth.IAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}")
public class AuthController extends BaseRestController{

    private final IAuthService authService;

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("login")
    public ResponseEntity<BaseResponse<AuthResponse>> login(@RequestBody LoginResquest request) {
        return execute(() -> {
            try {
                return authService.login(request);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @PostMapping("refresh-token")
    public ResponseEntity<BaseResponse<AuthResponse>> refreshToken(@RequestBody RefreshTokenResquest request) {
        return execute(() -> {
            try {
                return authService.refreshToken(request);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
