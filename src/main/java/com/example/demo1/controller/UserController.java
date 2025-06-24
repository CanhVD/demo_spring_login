package com.example.demo1.controller;

import com.example.demo1.exception.BindingResultException;
import com.example.demo1.model.User;
import com.example.demo1.request.UserRequest;
import com.example.demo1.response.BaseResponse;
import com.example.demo1.response.PageResponse;
import com.example.demo1.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController extends BaseRestController {

    private final UserService userService;

    private static final Logger logger = LogManager.getLogger(UserController.class);

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

    @GetMapping("/search-with-criteria")
    public ResponseEntity<?> advanceSearchWithCriteria(
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @RequestParam(defaultValue = "20", required = false) int pageSize,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "") String... search
    ) {
        logger.info("Request advance search query by criteria");
        return execute(() -> userService.searchWithCriteria(pageNo, pageSize, sortBy, search));
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
    public ResponseEntity<BaseResponse<User>> addUser(@Valid @RequestBody UserRequest request, BindingResult bindingResult) {
        return execute(() -> {
            if (bindingResult.hasErrors()) {
                throw new BindingResultException(bindingResult);
            }
            return userService.addUser(request);
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
