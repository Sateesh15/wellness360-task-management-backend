package com.wellness360.taskmanagement;

import com.wellness360.taskmanagement.dto.TaskDto;
import com.wellness360.taskmanagement.exception.TaskNotFoundException;
import com.wellness360.taskmanagement.model.Task;
import com.wellness360.taskmanagement.repository.TaskRepository;
import com.wellness360.taskmanagement.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for TaskService — covers critical business logic.
 */
@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task sampleTask;

    @BeforeEach
    void setUp() {
        sampleTask = Task.create("Test Task", "Test Description", LocalDate.now().plusDays(7));
    }

    // ─── Create Task ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("createTask: should create and return a task with PENDING status")
    void createTask_ShouldReturnCreatedTask() {
        when(taskRepository.save(any(Task.class))).thenReturn(sampleTask);

        TaskDto.CreateRequest req = new TaskDto.CreateRequest(
            "Test Task", "Test Description", LocalDate.now().plusDays(7)
        );
        TaskDto.Response response = taskService.createTask(req);

        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo("Test Task");
        assertThat(response.getStatus()).isEqualTo("pending");
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    // ─── Get All Tasks ───────────────────────────────────────────────────────

    @Test
    @DisplayName("getAllTasks: should return all tasks as DTOs")
    void getAllTasks_ShouldReturnListOfTasks() {
        when(taskRepository.findAll()).thenReturn(List.of(sampleTask));
        List<TaskDto.Response> results = taskService.getAllTasks();
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getTitle()).isEqualTo("Test Task");
    }

    @Test
    @DisplayName("getAllTasks: should return empty list when no tasks")
    void getAllTasks_WhenEmpty_ShouldReturnEmptyList() {
        when(taskRepository.findAll()).thenReturn(List.of());
        assertThat(taskService.getAllTasks()).isEmpty();
    }

    // ─── Get Task By Id ──────────────────────────────────────────────────────

    @Test
    @DisplayName("getTaskById: should return task when found")
    void getTaskById_WhenExists_ShouldReturnTask() {
        when(taskRepository.findById(sampleTask.getId())).thenReturn(Optional.of(sampleTask));
        TaskDto.Response result = taskService.getTaskById(sampleTask.getId());
        assertThat(result.getId()).isEqualTo(sampleTask.getId());
    }

    @Test
    @DisplayName("getTaskById: should throw TaskNotFoundException when not found")
    void getTaskById_WhenNotFound_ShouldThrow() {
        when(taskRepository.findById("bad-id")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> taskService.getTaskById("bad-id"))
            .isInstanceOf(TaskNotFoundException.class)
            .hasMessageContaining("bad-id");
    }

    // ─── Delete Task ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("deleteTask: should throw TaskNotFoundException when task doesn't exist")
    void deleteTask_WhenNotFound_ShouldThrow() {
        when(taskRepository.deleteById("missing")).thenReturn(false);
        assertThatThrownBy(() -> taskService.deleteTask("missing"))
            .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("deleteTask: should succeed when task exists")
    void deleteTask_WhenExists_ShouldNotThrow() {
        when(taskRepository.deleteById(sampleTask.getId())).thenReturn(true);
        assertThatCode(() -> taskService.deleteTask(sampleTask.getId())).doesNotThrowAnyException();
    }

    // ─── Mark Complete ───────────────────────────────────────────────────────

    @Test
    @DisplayName("markAsComplete: should set status to COMPLETED")
    void markAsComplete_ShouldSetStatusCompleted() {
        when(taskRepository.findById(sampleTask.getId())).thenReturn(Optional.of(sampleTask));
        when(taskRepository.save(any(Task.class))).thenReturn(sampleTask);

        TaskDto.Response result = taskService.markAsComplete(sampleTask.getId());
        assertThat(result.getStatus()).isEqualTo("completed");
    }
}
