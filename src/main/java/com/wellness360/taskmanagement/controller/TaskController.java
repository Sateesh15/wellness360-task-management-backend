package com.wellness360.taskmanagement.controller;

import com.wellness360.taskmanagement.dto.TaskDto;
import com.wellness360.taskmanagement.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for Task CRUD operations.
 * All endpoints require JWT authentication.
 */
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "Task management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class TaskController {

    private final TaskService taskService;

    /**
     * GET /api/tasks — Retrieve all tasks.
     */
    @GetMapping
    @Operation(summary = "Get all tasks")
    public ResponseEntity<TaskDto.ApiResponse<List<TaskDto.Response>>> getAllTasks() {
        List<TaskDto.Response> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(TaskDto.ApiResponse.success(
            "Retrieved " + tasks.size() + " task(s)", tasks
        ));
    }

    /**
     * GET /api/tasks/{id} — Get a specific task by ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get task by ID")
    public ResponseEntity<TaskDto.ApiResponse<TaskDto.Response>> getTaskById(@PathVariable String id) {
        return ResponseEntity.ok(
            TaskDto.ApiResponse.success("Task retrieved", taskService.getTaskById(id))
        );
    }

    /**
     * POST /api/tasks — Create a new task.
     */
    @PostMapping
    @Operation(summary = "Create a new task")
    public ResponseEntity<TaskDto.ApiResponse<TaskDto.Response>> createTask(
            @Valid @RequestBody TaskDto.CreateRequest request) {
        TaskDto.Response created = taskService.createTask(request);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(TaskDto.ApiResponse.success("Task created successfully", created));
    }

    /**
     * PUT /api/tasks/{id} — Update an existing task.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing task")
    public ResponseEntity<TaskDto.ApiResponse<TaskDto.Response>> updateTask(
            @PathVariable String id,
            @Valid @RequestBody TaskDto.UpdateRequest request) {
        return ResponseEntity.ok(
            TaskDto.ApiResponse.success("Task updated successfully", taskService.updateTask(id, request))
        );
    }

    /**
     * DELETE /api/tasks/{id} — Delete a task.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a task")
    public ResponseEntity<TaskDto.ApiResponse<Void>> deleteTask(@PathVariable String id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok(TaskDto.ApiResponse.success("Task deleted successfully", null));
    }

    /**
     * PATCH /api/tasks/{id}/complete — Mark task as completed.
     */
    @PatchMapping("/{id}/complete")
    @Operation(summary = "Mark a task as complete")
    public ResponseEntity<TaskDto.ApiResponse<TaskDto.Response>> markAsComplete(@PathVariable String id) {
        return ResponseEntity.ok(
            TaskDto.ApiResponse.success("Task marked as complete", taskService.markAsComplete(id))
        );
    }
}
