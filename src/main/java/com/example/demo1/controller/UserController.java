package com.example.demo1.controller;

import com.example.demo1.model.User;
import com.example.demo1.request.UserRequest;
import com.example.demo1.response.BaseResponse;
import com.example.demo1.response.PageResponse;
import com.example.demo1.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController extends BaseRestController {

    private final UserService userService;

    @GetMapping()
    public ResponseEntity<BaseResponse<List<User>>> getAllUsers() {
        return execute(userService::getAllUser);
    }

    @GetMapping("search")
    public ResponseEntity<BaseResponse<PageResponse<List<User>>>> getUserBySearch(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return execute(() -> userService.getUserBySearch(page, size));
    }

    @GetMapping("{id}")
    public ResponseEntity<BaseResponse<User>> getUserById(@PathVariable(value = "id") Integer id) {
        return execute(() -> {
            try {
                return userService.getUserById(id);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @PostMapping()
    public ResponseEntity<BaseResponse<User>> addUser(@RequestBody UserRequest request) {
        return execute(() -> {
            try {
                return userService.addUser(request);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @PutMapping({"{id}"})
    public ResponseEntity<BaseResponse<User>> updateUser(
            @RequestBody UserRequest request,
            @PathVariable(value = "id") Integer id
    ) {
        return execute(() -> {
            try {
                return userService.updateUser(request, id);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @DeleteMapping({"{id}"})
    public void deleteUser(@PathVariable(value = "id") Integer id) {
        userService.deleteUser(id);
    }
}
