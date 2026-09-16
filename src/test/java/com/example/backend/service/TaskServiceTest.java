package com.example.backend.service;

import com.example.backend.model.Task;
import com.example.backend.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void shouldReturnTaskWhenTaskExists() {

        Task task = new Task(1L, "Learn Spring", true);

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));

        Optional<Task> result = taskService.getTaskById(1L);

        assertTrue(result.isPresent());
        assertEquals("Learn Spring", result.get().getTitle());
        assertEquals(true, result.get().isCompleted());
    }

    @Test
    void shouldReturnEmptyWhenTaskDoesNotExist() {

        when(taskRepository.findById(99L))
                .thenReturn(Optional.empty());

        Optional<Task> result = taskService.getTaskById(99L);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnAllTasks() {

        Task task1 = new Task(1L, "Learn Spring", false);
        Task task2 = new Task(2L, "Learn Mockito", true);

        when(taskRepository.findAll())
                .thenReturn(List.of(task1, task2));

        List<Task> result = taskService.getAllTasks();

        assertEquals(2, result.size());
        assertEquals("Learn Spring", result.get(0).getTitle());
        assertEquals("Learn Mockito", result.get(1).getTitle());
    }

    @Test
    void shouldCreateTask() {

        Task task = new Task(null, "Learn Mockito", false);
        Task savedTask = new Task(1L, "Learn Mockito", false);

        when(taskRepository.save(any(Task.class)))
                .thenReturn(savedTask);

        Task result = taskService.createTask(task);

        assertEquals(1L, result.getId());
        assertEquals("Learn Mockito", result.getTitle());

        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void shouldDeleteTask() {
        taskService.deleteTask(1L);
        verify(taskRepository).deleteById(1L);
    }

    @Test
    void shouldReturnTrueWhenTaskExists() {

        when(taskRepository.existsById(1L))
                .thenReturn(true);

        boolean result = taskService.existsById(1L);

        assertTrue(result);
        verify(taskRepository).existsById(1L);
    }

    @Test
    void shouldReturnFalseWhenTaskDoesNotExist() {

        when(taskRepository.existsById(99L))
                .thenReturn(false);
        boolean result = taskService.existsById(99L);
        assertFalse(result);
    }

    @Test
    void shouldDeleteRandomTask() {
        taskService.deleteTask(5L);
        verify(taskRepository, times(1)).deleteById(5L);
    }

    @Test
    void shouldUpdateTask() {

        // Existing task in the "database"
        Task existingTask = new Task(1L, "Learn Spring", false);

        // Data coming from the client
        Task updatedTask = new Task(null, "Learn Mockito", true);

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(existingTask));

        when(taskRepository.save(any(Task.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        taskService.updateTask(1L, updatedTask);

        ArgumentCaptor<Task> taskCaptor =
                ArgumentCaptor.forClass(Task.class);

        verify(taskRepository).save(taskCaptor.capture());

        Task capturedTask = taskCaptor.getValue();

        assertEquals(1L, capturedTask.getId());
        assertEquals("Learn Mockito", capturedTask.getTitle());
        assertTrue(capturedTask.isCompleted());
    }

    @Test
    void shouldNotSaveWhenTaskDoesNotExist() {

        when(taskRepository.findById(99L))
                .thenReturn(Optional.empty());

        Task updatedTask = new Task();
        updatedTask.setTitle("New title");
        updatedTask.setCompleted(true);

        taskService.updateTask(99L, updatedTask);

        verify(taskRepository, times(1)).findById(99L);

        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void shouldReturnTaskPassedToSave() {

        Task task = new Task();
        task.setTitle("Mockito");

        when(taskRepository.save(any(Task.class)))
                .thenAnswer(invocation -> {
                    Task invocationTask = invocation.getArgument(0);
                    return invocationTask;
                });

        Task result = taskRepository.save(task);

        assertSame(task, result);
    }

    @Test
    void shouldCreateTaskWithId10() {

        Task task = new Task();
        task.setTitle("Learn Mockito");
        task.setCompleted(false);

        when(taskRepository.save(any(Task.class)))
                .thenAnswer(invocation -> {

                    Task savedTask = invocation.getArgument(0);

                    savedTask.setId(10L);

                    return  savedTask;
                });

        Task result = taskService.createTask(task);

        ArgumentCaptor<Task> captor =
                ArgumentCaptor.forClass(Task.class);

        verify(taskRepository).save(captor.capture());

        Task capturedTask = captor.getValue();

        assertEquals("Learn Mockito", capturedTask.getTitle());
        assertEquals(10L, result.getId());
    }
}
