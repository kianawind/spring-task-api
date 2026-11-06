package com.example.backend.repository;

import com.example.backend.model.Priority;
import com.example.backend.model.Task;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;

public class TaskPriorityOrder {

    public static Expression<Integer> rank(
            Root<Task> root,
            CriteriaBuilder cb
    ) {
        return cb.<Integer>selectCase()
                .when(cb.equal(root.get("priority"), Priority.LOW), 1)
                .when(cb.equal(root.get("priority"), Priority.MEDIUM), 2)
                .when(cb.equal(root.get("priority"), Priority.HIGH), 3)
                .otherwise(4);
    }
}
