package com.example.backend.controller;

import com.example.backend.dto.TaskRequest;
import com.example.backend.dto.TaskResponse;
import com.example.backend.mapper.TaskMapper;
import com.example.backend.model.Task;
import com.example.backend.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    public TaskController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @PostMapping("/tasks")
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest request) {
        Task task = taskMapper.toEntity(request);
        Task savedTask = taskService.createTask(task);
        TaskResponse response = taskMapper.toResponse(savedTask);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/tasks")
    public List<TaskResponse> getTasks() {
        return taskService.getAllTasks()
                .stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
//        return taskService.getTaskById(id)
//                .map(taskMapper::toResponse)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
        Task task = taskService.getTaskById(id);
        TaskResponse response = taskMapper.toResponse(task);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        if (!taskService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/tasks/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @Valid @RequestBody TaskRequest request) {
        Task updatedTask = taskMapper.toEntity(request);
        return taskService.updateTask(id, updatedTask)
                .map(taskMapper::toResponse)
                .map( ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
