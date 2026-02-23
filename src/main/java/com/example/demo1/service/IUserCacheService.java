package com.example.demo1.service;

import com.example.demo1.model.User;

public interface IUserCacheService {
    User getUserById(Long id);
    User getUserById2(Long id);
}
