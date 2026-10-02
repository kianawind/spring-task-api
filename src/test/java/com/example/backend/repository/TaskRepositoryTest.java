package com.example.backend.repository;

import com.example.backend.model.Priority;
import com.example.backend.model.Task;
import com.example.backend.specification.TaskSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void shouldFindTasksByTitleContains() {
        Task task1 = new Task(null, "Learn Java", false, Priority.HIGH);
        Task task2 = new Task(null, "Java Streams", true, Priority.MEDIUM);
        Task task3 = new Task(null, "Learn Mockito", false, Priority.LOW);

        taskRepository.saveAll(List.of(task1, task2, task3));

        List<Task> result = taskRepository.findAll(TaskSpecification.titleContains("java"));

        assertEquals(2, result.size());
        assertThat(result)
                .extracting(Task::getTitle)
                .containsExactlyInAnyOrder("Learn Java", "Java Streams");
    }

    @Test
    void shouldFindTasksByCombinedFilters() {

        Task task1 = new Task(null, "Learn Java", false, Priority.HIGH);
        Task task2 = new Task(null, "Java Streams", true, Priority.HIGH);
        Task task3 = new Task(null, "Learn Java Basics", false, Priority.LOW);
        Task task4 = new Task(null, "Learn Mockito", false, Priority.HIGH);

        taskRepository.saveAll(List.of(task1, task2, task3, task4));

        Specification<Task> specification = TaskSpecification.hasCompleted(false)
                .and(TaskSpecification.hasPriority(Priority.HIGH))
                .and(TaskSpecification.titleContains("java"));

        List<Task> result = taskRepository.findAll(specification);

        assertEquals(1, result.size());
        assertEquals("Learn Java", result.getFirst().getTitle());
        assertEquals(Priority.HIGH, result.getFirst().getPriority());
        assertFalse(result.getFirst().isCompleted());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t", "\n"})
    void shouldReturnAllTasksWhenTitleFilterIsBlank(String title) {
        Task task1 = new Task(null, "Learn Java", false, Priority.HIGH);
        Task task2 = new Task(null, "Learn Mockito", true, Priority.LOW);

        taskRepository.saveAll(List.of(task1, task2));
        Specification<Task> specification = TaskSpecification.titleContains(title);
        List<Task> result = taskRepository.findAll(specification);

        assertEquals(2, result.size());
        assertThat(result)
                .extracting(Task::getTitle)
                .containsExactlyInAnyOrder("Learn Java", "Learn Mockito");
    }

    @ParameterizedTest(name = "Search title: [{0}]")
    @ValueSource(strings = {
            "Java",
            " java",
            "JAVA ",
            "   JaVa  "
    })
    void shouldFindTasksIgnoringWhiteSpaceAndCase(String title) {
        Task task1 = new Task(null, "Learn Java", false, Priority.HIGH);
        Task task2 = new Task(null, "Learn Mockito", true, Priority.LOW);

        taskRepository.saveAll(List.of(task1, task2));
        List<Task> result = taskRepository.findAll(TaskSpecification.titleContains(title));
        assertEquals(1, result.size());
        assertEquals("Learn Java", result.getFirst().getTitle());
    }
}
