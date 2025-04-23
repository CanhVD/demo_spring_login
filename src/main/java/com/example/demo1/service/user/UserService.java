package com.example.demo1.service.user;

import com.example.demo1.criteria.SearchCriteria;
import com.example.demo1.criteria.SearchQueryCriteriaConsumer;
import com.example.demo1.model.User;
import com.example.demo1.repository.UserRepository;
import com.example.demo1.request.UserRequest;
import com.example.demo1.response.PageResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
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
import java.util.ArrayList;
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

    public PageResponse<?> searchWithCriteria(int offset, int pageSize, String sortBy, String address, String... search) {
        logger.info("Search user with search={} and sortBy={}", search, sortBy);

        List<SearchCriteria> criteriaList = new ArrayList<>();

        if (search.length > 0) {
            Pattern pattern = Pattern.compile("(\\w+?)(:|>|<)(.*)");
            for (String s : search) {
                Matcher matcher = pattern.matcher(s);
                if (matcher.find()) {
                    criteriaList.add(new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
                }
            }
        }

        if (StringUtils.hasLength(sortBy)) {
            Pattern pattern = Pattern.compile("(\\w+?)(:)(asc|desc)");
            for (String s : search) {
                Matcher matcher = pattern.matcher(s);
                if (matcher.find()) {
                    criteriaList.add(new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
                }
            }
        }

        List<User> users = getUsers(offset, pageSize, criteriaList, sortBy);

        Long totalElements = getTotalElements(criteriaList);

        Page<User> page = new PageImpl<>(users, PageRequest.of(offset, pageSize), totalElements);

        return PageResponse.<List<User>>builder()
                .result(page.getContent())
                .total(totalElements)
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
        if (user == null) {
            throw new Exception("User not found");
        }
        Boolean checkUserName = userRepository.existsByUsername(request.getUsername());
        if (checkUserName && !request.getUsername().equalsIgnoreCase(user.getUsername())) {
            throw new Exception("User is exits");
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

    private List<User> getUsers(int offset, int pageSize, List<SearchCriteria> criteriaList, String sortBy) {
        logger.info("-------------- getUsers --------------");

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> userRoot = query.from(User.class);

        Predicate userPredicate = criteriaBuilder.conjunction();
        SearchQueryCriteriaConsumer searchConsumer = new SearchQueryCriteriaConsumer(userPredicate, criteriaBuilder, userRoot);
        criteriaList.forEach(searchConsumer);
        userPredicate = searchConsumer.getPredicate();
        query.where(userPredicate);

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

    private Long getTotalElements(List<SearchCriteria> params) {
        logger.info("-------------- getTotalElements --------------");

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<User> root = query.from(User.class);

        Predicate predicate = criteriaBuilder.conjunction();
        SearchQueryCriteriaConsumer searchConsumer = new SearchQueryCriteriaConsumer(predicate, criteriaBuilder, root);
        params.forEach(searchConsumer);
        predicate = searchConsumer.getPredicate();
        query.select(criteriaBuilder.count(root));
        query.where(predicate);

        return entityManager.createQuery(query).getSingleResult();
    }
}
