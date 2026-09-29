package com.example.backend.specification;

import com.example.backend.model.Priority;
import com.example.backend.model.Task;
import org.springframework.data.jpa.domain.Specification;

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
}
