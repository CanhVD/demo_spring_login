package com.example.demo1.service.impl;

import org.springframework.stereotype.Service;

@Service
public class TestService {
    public Integer test() {
        throw new RuntimeException("Loi roi fix di");
    }
}
