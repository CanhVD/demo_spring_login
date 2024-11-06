package com.example.demo1.service.user;

import com.example.demo1.model.User;
import com.example.demo1.request.LoginResquest;
import com.example.demo1.request.UserRequest;
import com.example.demo1.response.LoginResponse;
import com.example.demo1.response.PageResponse;

import java.util.List;

public interface IUserService {

    List<User> getAllUser();

    PageResponse<List<User>> getUserBySearch(Integer page, Integer size);

    User getUserById(int id) throws Exception;

    LoginResponse login(LoginResquest resquest) throws Exception;

    User addUser(UserRequest request) throws Exception;

    User updateUser(UserRequest request, Integer id) throws Exception;

    void deleteUser(int id) throws Exception;
}
