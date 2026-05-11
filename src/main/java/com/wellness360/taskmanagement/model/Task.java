package com.wellness360.taskmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Task entity representing a task in the management system.
 * Uses in-memory storage (no database required).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    private String id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private TaskStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Factory method to create a new Task with generated ID and timestamps.
     */
    public static Task create(String title, String description, LocalDate dueDate) {
        LocalDateTime now = LocalDateTime.now();
        return Task.builder()
                .id(UUID.randomUUID().toString())
                .title(title)
                .description(description)
                .dueDate(dueDate)
                .status(TaskStatus.PENDING)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    /**
     * Mark task as updated by refreshing the updatedAt timestamp.
     */
    public void touch() {
        this.updatedAt = LocalDateTime.now();
    }
}
