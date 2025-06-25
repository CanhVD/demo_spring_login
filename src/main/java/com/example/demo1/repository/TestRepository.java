package com.example.demo1.repository;

import com.example.demo1.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TestRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public Long countAllUser() {
        TypedQuery<Long> query = entityManager.createQuery("select count(*) from User where username like '%ca%'", Long.class);
        return query.getSingleResult();
    }

    public List<User> getAllUser() {
        TypedQuery<User> query = entityManager.createQuery("select u from User u where username like '%ca%'", User.class);
        return query.getResultList();
    }

    public List<User> limitUser(int limit) {
        return entityManager.createQuery("SELECT u FROM User u ORDER BY u.id DESC ",
                User.class).setMaxResults(limit).getResultList();
    }
}
