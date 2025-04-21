package com.example.demo1.controller;

import com.example.demo1.model.User;
import com.example.demo1.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("test")
@RequiredArgsConstructor
public class TestController {

    private final TestRepository testRepository;

    @GetMapping
    public ResponseEntity<?> customeQuery() {
        return ResponseEntity.ok(testRepository.getAllUser());
    }
}
