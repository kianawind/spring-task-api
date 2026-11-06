package com.example.backend.repository;

import com.example.backend.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface TaskRepositoryCustom {

    Page<Task> findAllWithCustomSort(
            Specification<Task> specification,
            Pageable pageable
    );
}