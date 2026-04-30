package com.projects.sms.dtos;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;

import com.projects.sms.entity.Post;

public class PostSpecification {
	
    public static Specification<Post> filter(SearchRequest req) {

        return (Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // Keyword search (title + content)
            if (req.getKeyword() != null && !req.getKeyword().isEmpty()) {
                Predicate titleMatch = cb.like(
                        cb.lower(root.get("title")),
                        "%" + req.getKeyword().toLowerCase() + "%"
                );

                Predicate contentMatch = cb.like(
                        cb.lower(root.get("content")),
                        "%" + req.getKeyword().toLowerCase() + "%"
                );

                predicates.add(cb.or(titleMatch, contentMatch));
            }

            // Genre filter
            if (req.getGenre() != null && !req.getGenre().isEmpty()) {
                predicates.add(cb.equal(root.get("tag"), req.getGenre()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }


}
