package com.example.backend.service;

import com.example.backend.exception.TaskNotFoundException;
import com.example.backend.model.Priority;
import com.example.backend.model.Task;
import com.example.backend.repository.TaskRepository;
import com.example.backend.specification.TaskSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(Long id) {

        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    public  Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task updatedTask) {
        Task task = taskRepository.findById(id)
                .orElseThrow(()-> new TaskNotFoundException(id));
        task.setTitle(updatedTask.getTitle());
        task.setCompleted(updatedTask.isCompleted());
        task.setPriority(updatedTask.getPriority());
        return taskRepository.save(task);
    }

    public boolean existsById(Long id) {
        return taskRepository.existsById(id);
    }

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        taskRepository.deleteById(id);
    }

    public Page<Task> getTasks(Boolean completed, Priority priority, String titleContains, Pageable pageable) {
        Specification<Task> specification =
                TaskSpecification.hasCompleted(completed)
                        .and(TaskSpecification.hasPriority(priority))
                        .and(TaskSpecification.titleContains(titleContains));
        return taskRepository.findAll(specification, pageable);
    }
}
