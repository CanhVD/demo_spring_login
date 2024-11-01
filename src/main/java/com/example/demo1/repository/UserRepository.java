package com.example.demo1.repository;

import com.example.demo1.model.User;
import com.example.demo1.request.LoginResquest;
import com.example.demo1.response.LoginResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);

    Boolean existsByUsername(String username);
}
