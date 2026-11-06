package com.example.backend.repository;

import com.example.backend.model.Task;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TaskRepositoryCustomImpl implements TaskRepositoryCustom {

    private final EntityManager entityManager;

    public TaskRepositoryCustomImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Page<Task> findAllWithCustomSort(Specification<Task> specification, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        // 1. Create the query for retrieving tasks
        CriteriaQuery<Task> query = cb.createQuery(Task.class);
        Root<Task> root = query.from(Task.class);

        // 2. Apply filtering
        if (specification != null) {
            Predicate predicate = specification.toPredicate(root, query, cb);

            if (predicate != null) {
                query.where(predicate);
            }
        }

        // 3. Build sorting rules
        List<Order> orders = new ArrayList<>();

        for (Sort.Order sortOrder : pageable.getSort()) {
            String property = sortOrder.getProperty();

            if (property.equals("priority")) {
                Expression<Integer> rank = TaskPriorityOrder.rank(root, cb);

                // Always place null priorities last
                Expression<Integer> nullRank = cb.<Integer>selectCase()
                        .when(cb.isNull(root.get("priority")), 1)
                        .otherwise(0);

                orders.add(cb.asc(nullRank));

                // Sort non-null priorities by custom rank
                orders.add(
                        sortOrder.isAscending()
                                ? cb.asc(rank)
                                : cb.desc(rank)
                );
            } else {
                orders.add(
                        sortOrder.isAscending()
                                ? cb.asc(root.get(property))
                                : cb.desc(root.get(property))
                );
            }
        }

        if (!orders.isEmpty()) {
            query.orderBy(orders);
        }

        // 4. Create executable query
        TypedQuery<Task> typedQuery = entityManager.createQuery(query);

        // 5. Apply pagination
        if (pageable.isPaged()) {
            typedQuery.setFirstResult(Math.toIntExact(pageable.getOffset()));
            typedQuery.setMaxResults(pageable.getPageSize());
        }

        // 6. Execute query and retrieve tasks
        List<Task> tasks = typedQuery.getResultList();

        // 7. Create a separate COUNT query
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Task> countRoot = countQuery.from(Task.class);

        countQuery.select(cb.count(countRoot));

        // 8. Apply the same filters to the COUNT query
        if (specification != null) {
            Predicate countPredicate =
                    specification.toPredicate(countRoot, countQuery, cb);

            if (countPredicate != null) {
                countQuery.where(countPredicate);
            }
        }

        // 9. Count all matching tasks
        Long totalElements =
                entityManager.createQuery(countQuery).getSingleResult();

        // 10. Return a Page containing tasks and metadata
        return new PageImpl<>(tasks, pageable, totalElements);
    }
}