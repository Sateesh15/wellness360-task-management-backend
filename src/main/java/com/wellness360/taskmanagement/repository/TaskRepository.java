package com.wellness360.taskmanagement.repository;

import com.wellness360.taskmanagement.model.Task;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory repository for Task storage using ConcurrentHashMap for thread safety.
 * Simulates a database without actual persistence.
 */
@Repository
public class TaskRepository {

    private final Map<String, Task> store = new ConcurrentHashMap<>();

    /**
     * Save or update a task.
     * @param task the task to save
     * @return the saved task
     */
    public Task save(Task task) {
        store.put(task.getId(), task);
        return task;
    }

    /**
     * Find a task by its ID.
     * @param id the task ID
     * @return Optional containing the task if found
     */
    public Optional<Task> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    /**
     * Retrieve all tasks sorted by creation date (newest first).
     * @return list of all tasks
     */
    public List<Task> findAll() {
        return store.values().stream()
                .sorted(Comparator.comparing(Task::getCreatedAt).reversed())
                .toList();
    }

    /**
     * Delete a task by its ID.
     * @param id the task ID
     * @return true if deleted, false if not found
     */
    public boolean deleteById(String id) {
        return store.remove(id) != null;
    }

    /**
     * Check if a task exists by ID.
     * @param id the task ID
     * @return true if exists
     */
    public boolean existsById(String id) {
        return store.containsKey(id);
    }

    /**
     * Returns the count of all tasks.
     */
    public long count() {
        return store.size();
    }
}
