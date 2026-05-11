package com.wellness360.taskmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wellness360.taskmanagement.dto.TaskDto;
import com.wellness360.taskmanagement.exception.TaskNotFoundException;
import com.wellness360.taskmanagement.model.TaskStatus;
import com.wellness360.taskmanagement.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for TaskController — covers all CRUD and task management endpoints.
 * Uses MockMvc for HTTP layer testing and mocked TaskService.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("TaskController Tests")
class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private TaskDto.Response sampleTask1;
    private TaskDto.Response sampleTask2;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        // Initialize sample tasks
        sampleTask1 = TaskDto.Response.builder()
                .id("task-1")
                .title("Complete Project")
                .description("Finish the Spring Boot project")
                .dueDate(LocalDate.now().plusDays(7))
                .status("pending")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        sampleTask2 = TaskDto.Response.builder()
                .id("task-2")
                .title("Code Review")
                .description("Review pull requests")
                .dueDate(LocalDate.now().plusDays(3))
                .status("in-progress")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ────────────────── GET ALL TASKS ENDPOINT TESTS ──────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/tasks: should return all tasks successfully")
    void getAllTasks_ShouldReturnListOfTasks() throws Exception {
        // Arrange
        List<TaskDto.Response> tasks = Arrays.asList(sampleTask1, sampleTask2);
        when(taskService.getAllTasks()).thenReturn(tasks);

        // Act & Assert
        mockMvc.perform(get("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Retrieved 2 task(s)"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].id").value("task-1"))
                .andExpect(jsonPath("$.data[0].title").value("Complete Project"))
                .andExpect(jsonPath("$.data[1].id").value("task-2"))
                .andExpect(jsonPath("$.data[1].title").value("Code Review"));

        verify(taskService, times(1)).getAllTasks();
    }

    @Test
    @DisplayName("GET /api/tasks: should return empty list when no tasks exist")
    void getAllTasks_WhenNoTasks_ShouldReturnEmptyList() throws Exception {
        // Arrange
        when(taskService.getAllTasks()).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Retrieved 0 task(s)"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(0));

        verify(taskService, times(1)).getAllTasks();
    }

    @Test
    @DisplayName("GET /api/tasks: should include task status in response")
    void getAllTasks_ShouldIncludeTaskStatus() throws Exception {
        // Arrange
        List<TaskDto.Response> tasks = Arrays.asList(sampleTask1, sampleTask2);
        when(taskService.getAllTasks()).thenReturn(tasks);

        // Act & Assert
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].status").value("pending"))
                .andExpect(jsonPath("$.data[1].status").value("in-progress"));

        verify(taskService, times(1)).getAllTasks();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ──────────────── GET TASK BY ID ENDPOINT TESTS ──────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/tasks/{id}: should return task when found")
    void getTaskById_WhenExists_ShouldReturnTask() throws Exception {
        // Arrange
        when(taskService.getTaskById("task-1")).thenReturn(sampleTask1);

        // Act & Assert
        mockMvc.perform(get("/api/tasks/task-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Task retrieved"))
                .andExpect(jsonPath("$.data.id").value("task-1"))
                .andExpect(jsonPath("$.data.title").value("Complete Project"))
                .andExpect(jsonPath("$.data.description").value("Finish the Spring Boot project"));

        verify(taskService, times(1)).getTaskById("task-1");
    }

    @Test
    @DisplayName("GET /api/tasks/{id}: should return 404 when task not found")
    void getTaskById_WhenNotFound_ShouldReturn404() throws Exception {
        // Arrange
        when(taskService.getTaskById("invalid-id"))
                .thenThrow(new TaskNotFoundException("invalid-id"));

        // Act & Assert
        mockMvc.perform(get("/api/tasks/invalid-id"))
                .andExpect(status().isNotFound());

        verify(taskService, times(1)).getTaskById("invalid-id");
    }

    @Test
    @DisplayName("GET /api/tasks/{id}: should include all task details in response")
    void getTaskById_ShouldReturnAllTaskDetails() throws Exception {
        // Arrange
        when(taskService.getTaskById("task-1")).thenReturn(sampleTask1);

        // Act & Assert
        mockMvc.perform(get("/api/tasks/task-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.title").exists())
                .andExpect(jsonPath("$.data.description").exists())
                .andExpect(jsonPath("$.data.dueDate").exists())
                .andExpect(jsonPath("$.data.status").exists())
                .andExpect(jsonPath("$.data.createdAt").exists())
                .andExpect(jsonPath("$.data.updatedAt").exists());

        verify(taskService, times(1)).getTaskById("task-1");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ────────────────── CREATE TASK ENDPOINT TESTS ───────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("POST /api/tasks: should create a new task successfully")
    void createTask_WithValidData_ShouldReturnCreated() throws Exception {
        // Arrange
        TaskDto.CreateRequest createRequest = new TaskDto.CreateRequest(
                "New Task",
                "Task Description",
                LocalDate.now().plusDays(5)
        );

        when(taskService.createTask(any(TaskDto.CreateRequest.class))).thenReturn(sampleTask1);

        // Act & Assert
        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Task created successfully"))
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.title").value("Complete Project"));

        verify(taskService, times(1)).createTask(any(TaskDto.CreateRequest.class));
    }

    @Test
    @DisplayName("POST /api/tasks: should return 400 when title is missing")
    void createTask_WithMissingTitle_ShouldReturnBadRequest() throws Exception {
        // Arrange
        TaskDto.CreateRequest createRequest = new TaskDto.CreateRequest(
                "",
                "Task Description",
                LocalDate.now().plusDays(5)
        );

        // Act & Assert
        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isBadRequest());

        verify(taskService, never()).createTask(any());
    }

    @Test
    @DisplayName("POST /api/tasks: should return 400 when title is null")
    void createTask_WithNullTitle_ShouldReturnBadRequest() throws Exception {
        // Arrange
        String requestBody = "{\"description\": \"Task Description\", \"dueDate\": \"2025-12-31\"}";

        // Act & Assert
        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());

        verify(taskService, never()).createTask(any());
    }

    @Test
    @DisplayName("POST /api/tasks: should allow optional description")
    void createTask_WithoutDescription_ShouldSucceed() throws Exception {
        // Arrange
        TaskDto.CreateRequest createRequest = new TaskDto.CreateRequest(
                "Task Without Description",
                null,
                LocalDate.now().plusDays(5)
        );

        TaskDto.Response responseWithoutDesc = TaskDto.Response.builder()
                .id("task-3")
                .title("Task Without Description")
                .description(null)
                .dueDate(LocalDate.now().plusDays(5))
                .status("pending")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(taskService.createTask(any(TaskDto.CreateRequest.class))).thenReturn(responseWithoutDesc);

        // Act & Assert
        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true));

        verify(taskService, times(1)).createTask(any(TaskDto.CreateRequest.class));
    }

    @Test
    @DisplayName("POST /api/tasks: should return 400 when due date is in the past")
    void createTask_WithPastDueDate_ShouldReturnBadRequest() throws Exception {
        // Arrange
        TaskDto.CreateRequest createRequest = new TaskDto.CreateRequest(
                "Past Task",
                "Task Description",
                LocalDate.now().minusDays(1)
        );

        // Act & Assert
        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isBadRequest());

        verify(taskService, never()).createTask(any());
    }

    @Test
    @DisplayName("POST /api/tasks: should accept due date as today")
    void createTask_WithTodayDueDate_ShouldSucceed() throws Exception {
        // Arrange
        TaskDto.CreateRequest createRequest = new TaskDto.CreateRequest(
                "Today Task",
                "Task Description",
                LocalDate.now()
        );

        when(taskService.createTask(any(TaskDto.CreateRequest.class))).thenReturn(sampleTask1);

        // Act & Assert
        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated());

        verify(taskService, times(1)).createTask(any(TaskDto.CreateRequest.class));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ────────────────── UPDATE TASK ENDPOINT TESTS ───────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("PUT /api/tasks/{id}: should update task successfully")
    void updateTask_WithValidData_ShouldReturnUpdated() throws Exception {
        // Arrange
        TaskDto.UpdateRequest updateRequest = new TaskDto.UpdateRequest(
                "Updated Title",
                "Updated Description",
                LocalDate.now().plusDays(10),
                TaskStatus.COMPLETED
        );

        TaskDto.Response updatedTask = TaskDto.Response.builder()
                .id("task-1")
                .title("Updated Title")
                .description("Updated Description")
                .dueDate(LocalDate.now().plusDays(10))
                .status("completed")
                .createdAt(sampleTask1.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        when(taskService.updateTask("task-1", updateRequest)).thenReturn(updatedTask);

        // Act & Assert
        mockMvc.perform(put("/api/tasks/task-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Task updated successfully"))
                .andExpect(jsonPath("$.data.title").value("Updated Title"))
                .andExpect(jsonPath("$.data.status").value("completed"));

        verify(taskService, times(1)).updateTask(anyString(), any(TaskDto.UpdateRequest.class));
    }

    @Test
    @DisplayName("PUT /api/tasks/{id}: should return 404 when task not found")
    void updateTask_WhenNotFound_ShouldReturn404() throws Exception {
        // Arrange
        TaskDto.UpdateRequest updateRequest = new TaskDto.UpdateRequest(
                "Updated Title",
                "Updated Description",
                LocalDate.now().plusDays(10),
                TaskStatus.COMPLETED
        );

        when(taskService.updateTask("invalid-id", updateRequest))
                .thenThrow(new TaskNotFoundException("invalid-id"));

        // Act & Assert
        mockMvc.perform(put("/api/tasks/invalid-id")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());

        verify(taskService, times(1)).updateTask(anyString(), any(TaskDto.UpdateRequest.class));
    }

    @Test
    @DisplayName("PUT /api/tasks/{id}: should allow partial update")
    void updateTask_WithPartialData_ShouldUpdateOnlyProvidedFields() throws Exception {
        // Arrange
        TaskDto.UpdateRequest partialUpdate = new TaskDto.UpdateRequest(
                "Updated Title Only",
                null,
                null,
                null
        );

        TaskDto.Response partiallyUpdated = TaskDto.Response.builder()
                .id("task-1")
                .title("Updated Title Only")
                .description(sampleTask1.getDescription())
                .dueDate(sampleTask1.getDueDate())
                .status(sampleTask1.getStatus())
                .createdAt(sampleTask1.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        when(taskService.updateTask("task-1", partialUpdate)).thenReturn(partiallyUpdated);

        // Act & Assert
        mockMvc.perform(put("/api/tasks/task-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(partialUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("Updated Title Only"));

        verify(taskService, times(1)).updateTask("task-1", partialUpdate);
    }

    @Test
    @DisplayName("PUT /api/tasks/{id}: should update task status")
    void updateTask_ChangeStatus_ShouldUpdateSuccessfully() throws Exception {
        // Arrange
        TaskDto.UpdateRequest statusUpdate = new TaskDto.UpdateRequest(
                null,
                null,
                null,
                TaskStatus.IN_PROGRESS
        );

        TaskDto.Response statusUpdated = TaskDto.Response.builder()
                .id("task-1")
                .title(sampleTask1.getTitle())
                .description(sampleTask1.getDescription())
                .dueDate(sampleTask1.getDueDate())
                .status("in-progress")
                .createdAt(sampleTask1.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        when(taskService.updateTask("task-1", statusUpdate)).thenReturn(statusUpdated);

        // Act & Assert
        mockMvc.perform(put("/api/tasks/task-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(statusUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("in-progress"));

        verify(taskService, times(1)).updateTask("task-1", statusUpdate);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ────────────────── DELETE TASK ENDPOINT TESTS ───────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("DELETE /api/tasks/{id}: should delete task successfully")
    void deleteTask_WhenExists_ShouldReturnSuccess() throws Exception {
        // Arrange
        doNothing().when(taskService).deleteTask("task-1");

        // Act & Assert
        mockMvc.perform(delete("/api/tasks/task-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Task deleted successfully"))
                .andExpect(jsonPath("$.data").isEmpty());

        verify(taskService, times(1)).deleteTask("task-1");
    }

    @Test
    @DisplayName("DELETE /api/tasks/{id}: should return 404 when task not found")
    void deleteTask_WhenNotFound_ShouldReturn404() throws Exception {
        // Arrange
        doThrow(new TaskNotFoundException("invalid-id")).when(taskService).deleteTask("invalid-id");

        // Act & Assert
        mockMvc.perform(delete("/api/tasks/invalid-id"))
                .andExpect(status().isNotFound());

        verify(taskService, times(1)).deleteTask("invalid-id");
    }

    @Test
    @DisplayName("DELETE /api/tasks/{id}: should verify service was called exactly once")
    void deleteTask_ShouldCallServiceOnce() throws Exception {
        // Arrange
        doNothing().when(taskService).deleteTask("task-1");

        // Act
        mockMvc.perform(delete("/api/tasks/task-1"))
                .andExpect(status().isOk());

        // Assert
        verify(taskService, times(1)).deleteTask("task-1");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ──────────── MARK AS COMPLETE ENDPOINT TESTS ──────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("PATCH /api/tasks/{id}/complete: should mark task as completed")
    void markAsComplete_WhenExists_ShouldReturnCompleted() throws Exception {
        // Arrange
        TaskDto.Response completedTask = TaskDto.Response.builder()
                .id("task-1")
                .title(sampleTask1.getTitle())
                .description(sampleTask1.getDescription())
                .dueDate(sampleTask1.getDueDate())
                .status("completed")
                .createdAt(sampleTask1.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        when(taskService.markAsComplete("task-1")).thenReturn(completedTask);

        // Act & Assert
        mockMvc.perform(patch("/api/tasks/task-1/complete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Task marked as complete"))
                .andExpect(jsonPath("$.data.status").value("completed"))
                .andExpect(jsonPath("$.data.id").value("task-1"));

        verify(taskService, times(1)).markAsComplete("task-1");
    }

    @Test
    @DisplayName("PATCH /api/tasks/{id}/complete: should return 404 when task not found")
    void markAsComplete_WhenNotFound_ShouldReturn404() throws Exception {
        // Arrange
        when(taskService.markAsComplete("invalid-id"))
                .thenThrow(new TaskNotFoundException("invalid-id"));

        // Act & Assert
        mockMvc.perform(patch("/api/tasks/invalid-id/complete"))
                .andExpect(status().isNotFound());

        verify(taskService, times(1)).markAsComplete("invalid-id");
    }

    @Test
    @DisplayName("PATCH /api/tasks/{id}/complete: should update task timestamp")
    void markAsComplete_ShouldUpdateTimestamp() throws Exception {
        // Arrange
        LocalDateTime newTimestamp = LocalDateTime.now();
        TaskDto.Response completedWithTimestamp = TaskDto.Response.builder()
                .id("task-1")
                .title(sampleTask1.getTitle())
                .description(sampleTask1.getDescription())
                .dueDate(sampleTask1.getDueDate())
                .status("completed")
                .createdAt(sampleTask1.getCreatedAt())
                .updatedAt(newTimestamp)
                .build();

        when(taskService.markAsComplete("task-1")).thenReturn(completedWithTimestamp);

        // Act & Assert
        mockMvc.perform(patch("/api/tasks/task-1/complete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.updatedAt").exists());

        verify(taskService, times(1)).markAsComplete("task-1");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ────────────────── EDGE CASES & VALIDATION TESTS ──────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("POST /api/tasks: should accept title with special characters")
    void createTask_WithSpecialCharactersInTitle_ShouldSucceed() throws Exception {
        // Arrange
        TaskDto.CreateRequest createRequest = new TaskDto.CreateRequest(
                "Task: Complete & Review @ 50% #Important!",
                "Description",
                LocalDate.now().plusDays(5)
        );

        when(taskService.createTask(any(TaskDto.CreateRequest.class))).thenReturn(sampleTask1);

        // Act & Assert
        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated());

        verify(taskService, times(1)).createTask(any(TaskDto.CreateRequest.class));
    }

    @Test
    @DisplayName("POST /api/tasks: should reject title exceeding 200 characters")
    void createTask_WithExceedingTitleLength_ShouldReturnBadRequest() throws Exception {
        // Arrange
        String longTitle = "A".repeat(201);
        TaskDto.CreateRequest createRequest = new TaskDto.CreateRequest(
                longTitle,
                "Description",
                LocalDate.now().plusDays(5)
        );

        // Act & Assert
        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isBadRequest());

        verify(taskService, never()).createTask(any());
    }

    @Test
    @DisplayName("POST /api/tasks: should reject description exceeding 1000 characters")
    void createTask_WithExceedingDescriptionLength_ShouldReturnBadRequest() throws Exception {
        // Arrange
        String longDescription = "A".repeat(1001);
        TaskDto.CreateRequest createRequest = new TaskDto.CreateRequest(
                "Task Title",
                longDescription,
                LocalDate.now().plusDays(5)
        );

        // Act & Assert
        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isBadRequest());

        verify(taskService, never()).createTask(any());
    }

    @Test
    @DisplayName("GET /api/tasks: should verify correct content type is returned")
    void getAllTasks_ShouldReturnJsonContentType() throws Exception {
        // Arrange
        when(taskService.getAllTasks()).thenReturn(Arrays.asList(sampleTask1));

        // Act & Assert
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(taskService, times(1)).getAllTasks();
    }

    @Test
    @DisplayName("POST /api/tasks: should return 201 Created status")
    void createTask_ShouldReturnCreatedStatus() throws Exception {
        // Arrange
        TaskDto.CreateRequest createRequest = new TaskDto.CreateRequest(
                "New Task",
                "Description",
                LocalDate.now().plusDays(5)
        );

        when(taskService.createTask(any(TaskDto.CreateRequest.class))).thenReturn(sampleTask1);

        // Act & Assert
        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated());

        verify(taskService, times(1)).createTask(any(TaskDto.CreateRequest.class));
    }

    @Test
    @DisplayName("PUT /api/tasks/{id}: should return 200 OK status")
    void updateTask_ShouldReturnOkStatus() throws Exception {
        // Arrange
        TaskDto.UpdateRequest updateRequest = new TaskDto.UpdateRequest(
                "Updated",
                null,
                null,
                null
        );

        when(taskService.updateTask("task-1", updateRequest)).thenReturn(sampleTask1);

        // Act & Assert
        mockMvc.perform(put("/api/tasks/task-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());

        verify(taskService, times(1)).updateTask("task-1", updateRequest);
    }

    @Test
    @DisplayName("PATCH /api/tasks/{id}/complete: should only accept PATCH method")
    void markAsComplete_ShouldOnlyAcceptPatchMethod() throws Exception {
        // Arrange
        when(taskService.markAsComplete("task-1")).thenReturn(sampleTask1);

        // Act & Assert - Using GET should fail
        mockMvc.perform(get("/api/tasks/task-1/complete"))
                .andExpect(status().isMethodNotAllowed());

        // But PATCH should work
        mockMvc.perform(patch("/api/tasks/task-1/complete"))
                .andExpect(status().isOk());

        verify(taskService, times(1)).markAsComplete("task-1");
    }
}

