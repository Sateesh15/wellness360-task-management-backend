package com.wellness360.taskmanagement.service;

import com.wellness360.taskmanagement.dto.TaskDto;
import com.wellness360.taskmanagement.exception.TaskNotFoundException;
import com.wellness360.taskmanagement.model.Task;
import com.wellness360.taskmanagement.model.TaskStatus;
import com.wellness360.taskmanagement.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer containing all business logic for task management.
 */
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    /**
     * Retrieve all tasks.
     * @return list of all task responses
     */
    public List<TaskDto.Response> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Retrieve a task by ID.
     * @param id task identifier
     * @return task response
     * @throws TaskNotFoundException if task doesn't exist
     */
    public TaskDto.Response getTaskById(String id) {
        Task task = findOrThrow(id);
        return toResponse(task);
    }

    /**
     * Create a new task.
     * @param request creation payload
     * @return created task response
     */
    public TaskDto.Response createTask(TaskDto.CreateRequest request) {
        Task task = Task.create(
            request.getTitle().trim(),
            request.getDescription() != null ? request.getDescription().trim() : null,
            request.getDueDate()
        );
        return toResponse(taskRepository.save(task));
    }

    /**
     * Update an existing task (partial or full update).
     * @param id task identifier
     * @param request update payload
     * @return updated task response
     */
    public TaskDto.Response updateTask(String id, TaskDto.UpdateRequest request) {
        Task task = findOrThrow(id);

        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            task.setTitle(request.getTitle().trim());
        }
        if (request.getDescription() != null) {
            task.setDescription(request.getDescription().trim());
        }
        if (request.getDueDate() != null) {
            task.setDueDate(request.getDueDate());
        }
        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }

        task.touch();
        return toResponse(taskRepository.save(task));
    }

    /**
     * Delete a task by ID.
     * @param id task identifier
     * @throws TaskNotFoundException if task doesn't exist
     */
    public void deleteTask(String id) {
        if (!taskRepository.deleteById(id)) {
            throw new TaskNotFoundException(id);
        }
    }

    /**
     * Mark a task as completed.
     * @param id task identifier
     * @return updated task response
     */
    public TaskDto.Response markAsComplete(String id) {
        Task task = findOrThrow(id);
        task.setStatus(TaskStatus.COMPLETED);
        task.touch();
        return toResponse(taskRepository.save(task));
    }

    // ─── Private helpers ────────────────────────────────────────────────────────

    private Task findOrThrow(String id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    /**
     * Map Task entity to response DTO.
     */
    private TaskDto.Response toResponse(Task task) {
        return TaskDto.Response.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .dueDate(task.getDueDate())
                .status(task.getStatus().getValue())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }
}
