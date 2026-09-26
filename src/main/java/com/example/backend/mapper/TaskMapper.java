package com.example.backend.mapper;

import com.example.backend.dto.TaskRequest;
import com.example.backend.dto.TaskResponse;
import com.example.backend.model.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {
    public Task toEntity(TaskRequest request) {
        return new Task(null, request.getTitle(), request.isCompleted(), request.getPriority());
    }

    public TaskResponse toResponse(Task task) {
        return new TaskResponse(task.getId(), task.getTitle(), task.isCompleted(), task.getPriority());
    }
}
