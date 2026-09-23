package com.example.backend.controller;
import com.example.backend.dto.TaskRequest;
import com.example.backend.dto.TaskResponse;
import com.example.backend.exception.TaskNotFoundException;
import com.example.backend.model.Task;

import com.example.backend.mapper.TaskMapper;
import com.example.backend.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
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

    @Test
    void shouldReturn404WhenTaskDoesNotExist() throws Exception {

        when(taskService.getTaskById(99L))
                .thenThrow(new TaskNotFoundException(99L));

        mockMvc.perform(get("/tasks/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Task with id 99 not found"));
    }

    @Test
    void shouldCreateTask() throws Exception {

        Task task = new Task(null, "Learn MockMvc", false);
        Task savedTask = new Task(1L, "Learn MockMvc", false);

        TaskResponse response =
                new TaskResponse(1L, "Learn MockMvc", false);

        when(taskMapper.toEntity(any(TaskRequest.class)))
                .thenReturn(task);

        when(taskService.createTask(task))
                .thenReturn(savedTask);

        when(taskMapper.toResponse(savedTask))
                .thenReturn(response);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "title": "Learn MockMvc",
                              "completed": false
                            }
                            """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Learn MockMvc"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    void shouldReturn400WhenTitleIsBlank() throws Exception {
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "title": "",
                              "completed": false
                            }
                            """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title")
                        .value("Title must not be blank"));
        verifyNoInteractions(taskService);
        verifyNoInteractions(taskMapper);
    }

    @Test
    void shouldUpdateTask () throws  Exception {
        Task updatedTask = new Task(null, "Learn MockMvc", false);
        Task savedTask = new Task(1L, "Learn MockMvc update", true);

        TaskResponse response = new TaskResponse(1L,"Learn MockMvc update", true);

        when(taskMapper.toEntity(any(TaskRequest.class)))
                .thenReturn(updatedTask);

        when(taskService.updateTask(1L, updatedTask))
                .thenReturn(savedTask);

        when(taskMapper.toResponse(savedTask))
                .thenReturn(response);

        mockMvc.perform(put("/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "title": "Learn MockMvc update",
                        "completed": true
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Learn MockMvc update"))
                .andExpect(jsonPath("$.completed").value(true));

    }

    @Test
    void shouldReturn404WhenUpdatedTaskDoesNotExist() throws Exception {
        Task updatedTask = new Task(null, "Learn MockMvc", false);
        when(taskMapper.toEntity(any(TaskRequest.class)))
                .thenReturn(updatedTask);
        when(taskService.updateTask(99L, updatedTask))
                .thenThrow(new TaskNotFoundException(99L));

        mockMvc.perform(put("/tasks/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                   "title": "Learn MockMvc",
                                   "completed": false
                                }
                                """)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Task with id 99 not found"));
    }

    @Test
    void shouldDeleteWhenTaskExists() throws  Exception {
        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isNoContent());
        verify(taskService).deleteTask(1L);
    }

    @Test
    void deleteShouldThrowExceptionWhenTaskDoesNotExist() throws Exception{

        doThrow(new TaskNotFoundException(99L)).when(taskService).deleteTask(99L);
        mockMvc.perform(delete("/tasks/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Task with id 99 not found"));
        verify(taskService).deleteTask(99L);
    }
}
