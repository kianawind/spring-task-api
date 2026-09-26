package com.example.backend.dto;

import com.example.backend.model.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TaskRequest {

    @NotBlank(message = "Title must not be blank")
    private String title;

    private boolean completed;

    @NotNull(message = "Priority must not be null")
    private Priority priority;

    public TaskRequest() {
    }

    public TaskRequest(String title, boolean completed, Priority priority) {
        this.title = title;
        this.completed = completed;
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }
}
