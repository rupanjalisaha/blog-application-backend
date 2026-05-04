package com.projects.sms.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;

import com.projects.sms.dtos.SearchRequest;
import com.projects.sms.entity.Post;

public class PostSpecification {
	
    public static Specification<Post> filter(SearchRequest req) {

        return (Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            String keyword = "%" + req.getKeyword().toLowerCase() + "%";
            // Keyword search (title + content)
            if (req.getKeyword() != null && !req.getKeyword().isEmpty()) {
                Predicate titleMatch = cb.like(
                        cb.lower(root.get("postTitle")),
                        keyword
                );

                Predicate contentMatch = cb.like(
                        cb.lower(root.get("postBody")),
                        keyword
                );
                
                Predicate genreMatch = cb.like(
                        cb.lower(root.get("genre")),
                        keyword
                );

                predicates.add(cb.or(titleMatch, contentMatch, genreMatch));
            }

            // Genre filter
            if (req.getGenre() != null && !req.getGenre().isEmpty()) {
                predicates.add(cb.equal(root.get("genre"), req.getGenre()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }


}
