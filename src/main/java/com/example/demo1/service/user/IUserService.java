package com.example.demo1.service.user;

import com.example.demo1.model.User;
import com.example.demo1.request.LoginResquest;
import com.example.demo1.request.UserRequest;
import com.example.demo1.response.LoginResponse;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    List<User> getAllUser();

    User getUserById(int id) throws Exception;

    LoginResponse login(LoginResquest resquest) throws Exception;

    User addUser(UserRequest request) throws Exception;

    User updateUser(UserRequest request, Integer id) throws Exception;

    void deleteUser(int id) throws Exception;
}
