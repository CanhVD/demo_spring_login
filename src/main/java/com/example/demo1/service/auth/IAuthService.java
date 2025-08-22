package com.example.demo1.service.auth;

import com.example.demo1.request.LoginResquest;
import com.example.demo1.request.RefreshTokenResquest;
import com.example.demo1.response.AuthResponse;

public interface IAuthService {

    AuthResponse login(LoginResquest resquest);

    AuthResponse refreshToken(RefreshTokenResquest resquest);
}
