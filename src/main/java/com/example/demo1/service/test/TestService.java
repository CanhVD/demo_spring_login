package com.example.demo1.service.test;

import org.springframework.stereotype.Service;

@Service
public class TestService {
    public Integer test() {
        throw new RuntimeException("Loi roi fix di");
    }
}
