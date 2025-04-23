package com.example.demo1.util;

import com.example.demo1.criteria.SearchCriteria;
import com.example.demo1.criteria.SearchQueryCriteriaConsumer;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtil {

    // List ĐK search để query bất kỳ một entity nào
    public static <T> Predicate predicateBySearch(CriteriaBuilder criteriaBuilder, Root<T> root, String... search) {
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

        Predicate userPredicate = criteriaBuilder.conjunction();
        SearchQueryCriteriaConsumer searchQueryCriteriaConsumer = new SearchQueryCriteriaConsumer(userPredicate, criteriaBuilder, root);
        criteriaList.forEach(searchQueryCriteriaConsumer);

        return searchQueryCriteriaConsumer.getPredicate();
    }
}
