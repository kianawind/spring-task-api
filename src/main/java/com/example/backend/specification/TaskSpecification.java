package com.example.backend.specification;

import com.example.backend.model.Priority;
import com.example.backend.model.Task;
import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;

public class TaskSpecification {
    public static Specification<Task> hasCompleted(Boolean completed) {
        return (root, query, criteriaBuilder) ->
        {
            if (completed == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("completed"), completed);
        };
    }

    public static Specification<Task> hasPriority(Priority priority) {
        return (root, query, criteriaBuilder) ->
        {
            if (priority == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("priority"), priority);
        };
    }

    public static Specification<Task> titleContains(String title) {
        return (root, query, criteriaBuilder) ->
        {
            if (title == null || title.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            String searchTitle = title.strip().toLowerCase(Locale.ROOT);
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + searchTitle + "%");
        };
    }

}
