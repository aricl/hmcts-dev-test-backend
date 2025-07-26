package uk.gov.hmcts.reform.dev.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.hmcts.reform.dev.TaskRepository;
import uk.gov.hmcts.reform.dev.models.Task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Task sampleTask() {
        return new Task(
            1L,
            "Wear a hat",
            "All day",
            "OPEN",
            LocalDateTime.of(2025, 7, 26, 12, 0)
        );
    }

    @Test
    @DisplayName("should create a new task")
    void createTask() throws Exception {
        Task task = sampleTask();
        task.setId(null); // ID is null for creation
        Task savedTask = sampleTask();

        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        mockMvc.perform(post("/task")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(task)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(savedTask.getId()))
            .andExpect(jsonPath("$.title").value(savedTask.getTitle()))
            .andExpect(jsonPath("$.status").value(savedTask.getStatus()));
    }

    @Test
    @DisplayName("should get a task by id")
    void getTaskById() throws Exception {
        Task task = sampleTask();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        mockMvc.perform(get("/task/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(task.getId()))
            .andExpect(jsonPath("$.title").value(task.getTitle()));
    }

    @Test
    @DisplayName("should return 404 for non-existing task")
    void getTaskByIdNotFound() throws Exception {
        when(taskRepository.findById(42L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/task/42"))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should get all tasks")
    void getTasks() throws Exception {
        List<Task> tasks = List.of(sampleTask());
        when(taskRepository.findAll()).thenReturn(tasks);

        mockMvc.perform(get("/task"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(tasks.get(0).getId()));
    }

    @Test
    @DisplayName("should update a task status")
    void updateTaskStatus() throws Exception {
        Task existingTask = sampleTask();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        // No .save() call in controller for update (just setStatus on entity)

        mockMvc.perform(put("/task/1/status")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("\"DONE\""))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(existingTask.getId()))
            .andExpect(jsonPath("$.status").value("\"DONE\""));
    }

    @Test
    @DisplayName("should return 404 when updating non-existent task status")
    void updateTaskStatusNotFound() throws Exception {
        when(taskRepository.findById(123L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/task/123/status")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("\"DONE\""))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should delete a task by id")
    void deleteTask() throws Exception {
        Task task = sampleTask();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        mockMvc.perform(delete("/task/1"))
            .andExpect(status().isOk())
            .andExpect(content().string(""));

        verify(taskRepository).delete(task);
    }

    @Test
    @DisplayName("should return 404 when deleting non-existent task")
    void deleteTaskNotFound() throws Exception {
        when(taskRepository.findById(42L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/task/42"))
            .andExpect(status().isNotFound());
    }
}
