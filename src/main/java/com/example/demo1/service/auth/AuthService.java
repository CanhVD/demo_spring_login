package com.example.demo1.service.auth;

import com.example.demo1.model.User;
import com.example.demo1.repository.UserRepository;
import com.example.demo1.request.LoginResquest;
import com.example.demo1.request.RefreshTokenResquest;
import com.example.demo1.response.AuthResponse;
import com.example.demo1.service.jwt.JwtService;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements IAuthService {

    private final UserRepository userRepository;

    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public AuthResponse login(LoginResquest resquest) {
        User user = userRepository.findByUsername(resquest.getUsername()).orElse(null);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return AuthResponse.builder()
                .username(resquest.getUsername())
                .accessToken(jwtService.generateToken(user))
                .refreshToken(jwtService.generateRefreshToken(user))
                .build();
    }

    public AuthResponse refreshToken(RefreshTokenResquest resquest) {
        String userName = jwtService.extractUsername(resquest.getRefreshToken());
        User user = userRepository.findByUsername(userName).orElse(null);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return AuthResponse.builder()
                .username(userName)
                .accessToken(jwtService.generateToken(user))
                .refreshToken(jwtService.generateRefreshToken(user))
                .build();
    }
}
