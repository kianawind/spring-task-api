package com.example.backend.mapper;

import com.example.backend.dto.TaskRequest;
import com.example.backend.dto.TaskResponse;
import com.example.backend.model.Priority;
import com.example.backend.model.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TaskMapperTest {
    TaskMapper taskMapper = new TaskMapper();

    @Test
    void shouldMapRequestToEntity() {
        TaskRequest taskRequest = new TaskRequest("Title", true, Priority.HIGH);
        Task task = taskMapper.toEntity(taskRequest);
        assertNull(task.getId());
        assertEquals("Title", task.getTitle());
        assertTrue(task.isCompleted());
        assertEquals(Priority.HIGH, task.getPriority());
    }

    @Test
    void shouldMapEntityToResponse() {
        Task task = new Task(5L, "Test Mapper", false, Priority.MEDIUM);
        TaskResponse taskResponse = taskMapper.toResponse(task);
        assertEquals(5L, taskResponse.getId());
        assertEquals("Test Mapper", taskResponse.getTitle());
        assertFalse(taskResponse.isCompleted());
        assertEquals(Priority.MEDIUM, taskResponse.getPriority());
    }
}
