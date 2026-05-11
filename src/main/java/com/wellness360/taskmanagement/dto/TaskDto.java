package com.wellness360.taskmanagement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wellness360.taskmanagement.model.TaskStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTOs for Task API request/response payloads.
 */
public class TaskDto {

    /**
     * Request body for creating a new task.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        @NotBlank(message = "Title is required")
        @Size(min = 1, max = 200, message = "Title must be between 1 and 200 characters")
        private String title;

        @Size(max = 1000, message = "Description cannot exceed 1000 characters")
        private String description;

        @JsonFormat(pattern = "yyyy-MM-dd")
        @FutureOrPresent(message = "Due date must be today or in the future")
        private LocalDate dueDate;
    }

    /**
     * Request body for updating an existing task.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        @Size(min = 1, max = 200, message = "Title must be between 1 and 200 characters")
        private String title;

        @Size(max = 1000, message = "Description cannot exceed 1000 characters")
        private String description;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate dueDate;

        private TaskStatus status;
    }

    /**
     * Response payload for a task.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private String id;
        private String title;
        private String description;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate dueDate;

        private String status;

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime createdAt;

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime updatedAt;
    }

    /**
     * Standard API response wrapper.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApiResponse<T> {
        private boolean success;
        private String message;
        private T data;

        public static <T> ApiResponse<T> success(String message, T data) {
            return ApiResponse.<T>builder()
                    .success(true)
                    .message(message)
                    .data(data)
                    .build();
        }

        public static <T> ApiResponse<T> error(String message) {
            return ApiResponse.<T>builder()
                    .success(false)
                    .message(message)
                    .build();
        }
    }
}
