package com.example.demo1.service.impl;

import com.example.demo1.model.User;
import com.example.demo1.util.DataUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class BaseService {
    @PersistenceContext
    private EntityManager entityManager;

    protected <T> List<T> getListBySearch(int offset, int pageSize, String sortBy, Class<T> clazz, String... search) {
        log.info("-------------- getListBySearch --------------");

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = criteriaBuilder.createQuery(clazz);
        Root<T> userRoot = query.from(clazz);
        query.where(DataUtil.predicateBySearch(criteriaBuilder, userRoot, search));

        Pattern pattern = Pattern.compile("(\\w+?)(:)(asc|desc)");
        if (StringUtils.hasLength(sortBy)) {
            Matcher matcher = pattern.matcher(sortBy);
            if (matcher.find()) {
                String fieldName = matcher.group(1);
                String direction = matcher.group(3);
                if (direction.equalsIgnoreCase("asc")) {
                    query.orderBy(criteriaBuilder.asc(userRoot.get(fieldName)));
                } else {
                    query.orderBy(criteriaBuilder.desc(userRoot.get(fieldName)));
                }
            }
        }

        return entityManager.createQuery(query)
                .setFirstResult(offset)
                .setMaxResults(pageSize)
                .getResultList();
    }

    protected Long getTotalElements(String... search) {
        log.info("-------------- getTotalElements --------------");

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<User> root = query.from(User.class);
        query.select(criteriaBuilder.count(root));
        query.where(DataUtil.predicateBySearch(criteriaBuilder, root, search));

        return entityManager.createQuery(query).getSingleResult();
    }
}
