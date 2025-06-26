package com.example.demo1.controller;

import com.example.demo1.exception.BindingResultException;
import com.example.demo1.model.User;
import com.example.demo1.repository.TestRepository;
import com.example.demo1.repository.UserRepository;
import com.example.demo1.request.UserRequest;
import com.example.demo1.service.test.TestService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.google.gson.Gson;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("test")
@RequiredArgsConstructor
@PropertySource({"classpath:test.properties"})
public class TestController extends BaseRestController {

    private final TestRepository testRepository;

    private final UserRepository userRepository;

    private final TestService testService;

    private final Gson gson;

    private final Environment env;

    @GetMapping
    public ResponseEntity<?> customeQuery() {
        log.error("Hello, {Name}!", "World");
        //return ResponseEntity.ok(testRepository.getAllUser());
        return ResponseEntity.ok(env.getProperty("jdbc.user"));
    }

    @GetMapping("user")
    public ResponseEntity<?> limitUser(@RequestParam(defaultValue = "0") int limit) {
        return ResponseEntity.ok(testRepository.limitUser(limit));
    }

    @GetMapping("upload")
    public ResponseEntity<?> saveUser(@ModelAttribute UserRequest request) {
        String str = MessageFormat.format("{0} {1}", request.getUsername(), request.getPassword());
        return ResponseEntity.ok(request);
    }

    @PostMapping
    public ResponseEntity<?> test(@RequestBody User request) {
        request.setPassword("hehehe");
        log.error("Loi roi fix dii");
        return ResponseEntity.ok(request);
    }

    @GetMapping("ok")
    public ResponseEntity<?> ok(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("user")) {
                    String json = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                    return ResponseEntity.ok(gson.fromJson(json, User.class));
                }
            }
        }

        User user = new User();
        //test2(user);
        return ResponseEntity.ok(user);
    }

    @PostMapping("user/add")
    public ResponseEntity<?> addUser(
//            @Valid @RequestBody User user,
//            BindingResult bindingResult,
            @RequestParam LocalDate localDate,
            HttpServletResponse response,
            HttpServletRequest request
    ) {
        try {
            // Get Cookie
//            Cookie[] cookies = request.getCookies();
//            if (cookies != null) {
//                for (Cookie cookie : cookies) {
//                    if (cookie.getName().equals("username")) {
//                        return ResponseEntity.ok(cookie.getValue());
//                    }
//                }
//            }
            User user = userRepository.findByUsername("George Kelly").orElse(new User());
            String test = gson.toJson(user);
            Cookie cookie = new Cookie("user", URLEncoder.encode(gson.toJson(user), StandardCharsets.UTF_8));
            cookie.setHttpOnly(true);
            cookie.setSecure(false);
            cookie.setMaxAge(24 * 60 * 60); // Muon xoa cookie thi setMaxAge(0), Cookie tu dong bi xoa khi het setMaxAge
            cookie.setPath("/");
            response.addCookie(cookie);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("ok");
    }

    @ExceptionHandler(BindingResultException.class)
    public ResponseEntity<?> showInputNotAcceptable() {
        return ResponseEntity.badRequest().body("Loi roi fix dii");
    }


    private void test2(User user5) {
        User user2 = userRepository.findByUsername("George Kelly").orElse(new User());
        User user3 = userRepository.findById(1234).orElse(new User());
        BeanUtils.copyProperties(user2, user5);
    }
}

