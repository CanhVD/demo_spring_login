package com.example.demo1.service.impl;

import com.example.demo1.anotation.CacheException;
import com.example.demo1.model.User;
import com.example.demo1.service.IUserCacheService;
import org.springframework.stereotype.Service;

@Service
public class UserCacheService implements IUserCacheService {
    // Return exception because anotation @CacheException
    @Override
    @CacheException
    public User getUserById(Long id) {
        throw new RuntimeException("custom error by redis");
    }

    // No return exception but log error
    @Override
    public User getUserById2(Long id) {
        throw new RuntimeException("custom error by redis");
    }
}
