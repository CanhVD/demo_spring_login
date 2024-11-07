package com.example.demo1.service.user;

import com.example.demo1.model.User;
import com.example.demo1.repository.UserRepository;
import com.example.demo1.request.UserRequest;
import com.example.demo1.request.LoginResquest;
import com.example.demo1.response.LoginResponse;
import com.example.demo1.response.PageResponse;
import com.example.demo1.service.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;

    private final JwtService jwtService;

    private static final Logger logger = LogManager.getLogger(UserService.class);

    @Override
    public List<User> getAllUser() {
        logger.info("api get all user is running");
        logger.debug("api get all user is running");
        return userRepository.findAll();
    }

    @Override
    public PageResponse<List<User>> getUserBySearch(Integer page, Integer size) {
        Page<User> userPage = userRepository.findAll(PageRequest.of(page - 1, size));
        System.out.println(userPage.getTotalElements());
        return PageResponse.<List<User>>builder()
                .result(userPage.getContent())
                .total(userPage.getTotalElements())
                .build();
    }

    @Override
    public User getUserById(int id) throws Exception {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new Exception("User not found");
        }
        return user;
    }

    public LoginResponse login(LoginResquest resquest) throws Exception {
        User user = userRepository.findByUsername(resquest.getUsername());
        if (user == null) {
            throw new Exception("User not found");
        }
        return LoginResponse.builder()
                .username(resquest.getUsername())
                .accessToken(jwtService.generateToken(user))
                .build();
    }

    @Override
    public User addUser(UserRequest request) throws Exception {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new Exception("User is exists");
        }
        User newUser = User.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .email(request.getEmail())
                .createdAt(LocalDateTime.now())
                .createBy(request.getCreateBy())
                .build();
        return userRepository.save(newUser);
    }

    @Override
    public User updateUser(UserRequest request, Integer id) throws Exception {
        User user = userRepository.findById(id).orElse(null);
        if (user != null && !user.getUsername().equalsIgnoreCase(request.getUsername())) {
            throw new Exception("User is exists");
        }
        User updateUser = User.builder()
                .id(user.getId())
                .username(request.getUsername())
                .password(request.getPassword())
                .email(request.getEmail())
                .updatedAt(LocalDateTime.now())
                .updateBy(request.getCreateBy())
                .createBy(user.getCreateBy())
                .createdAt(user.getCreatedAt())
                .build();
        return userRepository.save(updateUser);
    }

    @Override
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }
}
