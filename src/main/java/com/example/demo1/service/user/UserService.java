package com.example.demo1.service.user;

import com.example.demo1.model.User;
import com.example.demo1.repository.UserRepository;
import com.example.demo1.request.UserRequest;
import com.example.demo1.response.PageResponse;
import com.example.demo1.util.DataUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    @PersistenceContext
    private EntityManager entityManager;

    private final UserRepository userRepository;

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

    public PageResponse<?> searchWithCriteria(int offset, int pageSize, String sortBy, String... search) {
        logger.info("Search user with search={} and sortBy={}", search, sortBy);
        // Get list user by criteria
        List<User> users = getListUsers(offset, pageSize, sortBy, search);
        // Get total record
        Long totalElements = getTotalElements(search);

        Page<User> page = new PageImpl<>(users, PageRequest.of(offset, pageSize), totalElements);

        return PageResponse.<List<User>>builder()
                .total(totalElements)
                .result(page.getContent())
                .build();
    }

    @Override
    public User getUserById(int id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return user;
    }

    @Override
    public User addUser(UserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("User is exists");
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
    public User updateUser(UserRequest request, Integer id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        Boolean checkUserName = userRepository.existsByUsername(request.getUsername());
        if (checkUserName && !request.getUsername().equalsIgnoreCase(user.getUsername())) {
            throw new RuntimeException("User is exits");
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

    private List<User> getListUsers(int offset, int pageSize, String sortBy, String... search) {
        logger.info("-------------- getUsers --------------");

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> userRoot = query.from(User.class);
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

    private Long getTotalElements(String... search) {
        logger.info("-------------- getTotalElements --------------");

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<User> root = query.from(User.class);
        query.select(criteriaBuilder.count(root));
        query.where(DataUtil.predicateBySearch(criteriaBuilder, root, search));

        return entityManager.createQuery(query).getSingleResult();
    }
}
