package com.example.backend.controller;
import com.example.backend.dto.TaskResponse;
import com.example.backend.model.Task;

import com.example.backend.mapper.TaskMapper;
import com.example.backend.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @MockitoBean
    private TaskMapper taskMapper;

    @Test
    void shouldReturnTaskWhenTaskExists() throws Exception {

        Task task = new Task(1L, "Learn MockMvc", false);

        TaskResponse response =
                new TaskResponse(1L, "Learn MockMvc", false);

        when(taskService.getTaskById(1L))
                .thenReturn(task);

        when(taskMapper.toResponse(task))
                .thenReturn(response);

        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Learn MockMvc"))
                .andExpect(jsonPath("$.completed").value(false));
    }
}
