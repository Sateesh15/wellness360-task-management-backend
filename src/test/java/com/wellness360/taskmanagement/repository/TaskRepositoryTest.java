package com.wellness360.taskmanagement.repository;

import com.wellness360.taskmanagement.model.Task;
import com.wellness360.taskmanagement.model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for TaskRepository — covers all CRUD operations, in-memory storage,
 * thread safety with ConcurrentHashMap, and edge cases.
 */
@DisplayName("TaskRepository Tests")
class TaskRepositoryTest {

    private TaskRepository taskRepository;
    private Task sampleTask1;
    private Task sampleTask2;
    private Task sampleTask3;

    @BeforeEach
    void setUp() {
        taskRepository = new TaskRepository();

        // Create sample tasks with different timestamps
        sampleTask1 = Task.builder()
                .id("task-1")
                .title("Complete Project")
                .description("Finish Spring Boot project")
                .dueDate(LocalDate.now().plusDays(7))
                .status(TaskStatus.PENDING)
                .createdAt(LocalDateTime.now().minusHours(5))
                .updatedAt(LocalDateTime.now().minusHours(5))
                .build();

        sampleTask2 = Task.builder()
                .id("task-2")
                .title("Code Review")
                .description("Review pull requests")
                .dueDate(LocalDate.now().plusDays(3))
                .status(TaskStatus.IN_PROGRESS)
                .createdAt(LocalDateTime.now().minusHours(2))
                .updatedAt(LocalDateTime.now().minusHours(2))
                .build();

        sampleTask3 = Task.builder()
                .id("task-3")
                .title("Write Tests")
                .description("Unit and integration tests")
                .dueDate(LocalDate.now().plusDays(10))
                .status(TaskStatus.COMPLETED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ────────────────── SAVE METHOD TESTS ────────────────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("save: should save a task successfully")
    void save_ShouldSaveTaskSuccessfully() {
        // Act
        Task savedTask = taskRepository.save(sampleTask1);

        // Assert
        assertThat(savedTask).isNotNull();
        assertThat(savedTask).isEqualTo(sampleTask1);
        assertThat(savedTask.getId()).isEqualTo("task-1");
    }

    @Test
    @DisplayName("save: should return the saved task")
    void save_ShouldReturnTheSavedTask() {
        // Act
        Task savedTask = taskRepository.save(sampleTask1);

        // Assert
        assertThat(savedTask).isSameAs(sampleTask1);
    }

    @Test
    @DisplayName("save: should store task in repository")
    void save_ShouldStoreTaskInRepository() {
        // Act
        taskRepository.save(sampleTask1);

        // Assert
        Optional<Task> foundTask = taskRepository.findById("task-1");
        assertThat(foundTask).isPresent();
        assertThat(foundTask.get()).isEqualTo(sampleTask1);
    }

    @Test
    @DisplayName("save: should save multiple tasks")
    void save_ShouldSaveMultipleTasks() {
        // Act
        taskRepository.save(sampleTask1);
        taskRepository.save(sampleTask2);
        taskRepository.save(sampleTask3);

        // Assert
        assertThat(taskRepository.count()).isEqualTo(3);
    }

    @Test
    @DisplayName("save: should update task when id already exists")
    void save_ShouldUpdateTaskWhenIdExists() {
        // Arrange
        taskRepository.save(sampleTask1);
        sampleTask1.setTitle("Updated Title");

        // Act
        taskRepository.save(sampleTask1);

        // Assert
        Optional<Task> foundTask = taskRepository.findById("task-1");
        assertThat(foundTask.get().getTitle()).isEqualTo("Updated Title");
    }

    @Test
    @DisplayName("save: should overwrite existing task with same id")
    void save_ShouldOverwriteExistingTaskWithSameId() {
        // Arrange
        taskRepository.save(sampleTask1);
        Task updatedTask = Task.builder()
                .id("task-1")
                .title("New Title")
                .build();

        // Act
        taskRepository.save(updatedTask);

        // Assert
        assertThat(taskRepository.count()).isEqualTo(1);
        Optional<Task> foundTask = taskRepository.findById("task-1");
        assertThat(foundTask.get().getTitle()).isEqualTo("New Title");
    }

    @Test
    @DisplayName("save: should handle null description")
    void save_WithNullDescription_ShouldSave() {
        // Arrange
        Task taskWithoutDesc = Task.builder()
                .id("task-no-desc")
                .title("Task Without Description")
                .description(null)
                .build();

        // Act
        taskRepository.save(taskWithoutDesc);

        // Assert
        Optional<Task> foundTask = taskRepository.findById("task-no-desc");
        assertThat(foundTask).isPresent();
        assertThat(foundTask.get().getDescription()).isNull();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ────────────── FIND BY ID METHOD TESTS ────────────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("findById: should find existing task")
    void findById_ShouldFindExistingTask() {
        // Arrange
        taskRepository.save(sampleTask1);

        // Act
        Optional<Task> foundTask = taskRepository.findById("task-1");

        // Assert
        assertThat(foundTask).isPresent();
        assertThat(foundTask.get()).isEqualTo(sampleTask1);
    }

    @Test
    @DisplayName("findById: should return empty Optional for non-existent task")
    void findById_ShouldReturnEmptyForNonExistent() {
        // Act
        Optional<Task> foundTask = taskRepository.findById("non-existent");

        // Assert
        assertThat(foundTask).isEmpty();
    }

    @Test
    @DisplayName("findById: should find task by exact id match")
        void findById_ShouldFindByExactIdMatch() {
        // Arrange
        taskRepository.save(sampleTask1);

        // Act
        Optional<Task> foundTask = taskRepository.findById("task-1");

        // Assert
        assertThat(foundTask).isPresent();
        assertThat(foundTask.get().getId()).isEqualTo("task-1");
    }

    @Test
    @DisplayName("findById: should not find task with similar but different id")
    void findById_ShouldNotFindSimilarIds() {
        // Arrange
        taskRepository.save(sampleTask1);

        // Act
        Optional<Task> foundTask = taskRepository.findById("task-11");

        // Assert
        assertThat(foundTask).isEmpty();
    }

    @Test
    @DisplayName("findById: should be case-sensitive for id lookup")
    void findById_ShouldBeCaseSensitiveForId() {
        // Arrange
        taskRepository.save(sampleTask1);

        // Act
        Optional<Task> foundTask = taskRepository.findById("TASK-1");

        // Assert
        assertThat(foundTask).isEmpty();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ────────────── FIND ALL METHOD TESTS ────────────────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("findAll: should return empty list when no tasks exist")
    void findAll_ShouldReturnEmptyListWhenNoTasks() {
        // Act
        List<Task> tasks = taskRepository.findAll();

        // Assert
        assertThat(tasks).isEmpty();
    }

    @Test
    @DisplayName("findAll: should return all saved tasks")
    void findAll_ShouldReturnAllTasks() {
        // Arrange
        taskRepository.save(sampleTask1);
        taskRepository.save(sampleTask2);
        taskRepository.save(sampleTask3);

        // Act
        List<Task> tasks = taskRepository.findAll();

        // Assert
        assertThat(tasks).hasSize(3);
        assertThat(tasks).contains(sampleTask1, sampleTask2, sampleTask3);
    }

    @Test
    @DisplayName("findAll: should return tasks sorted by creation date (newest first)")
    void findAll_ShouldReturnTasksSortedByCreationDateNewestFirst() {
        // Arrange
        taskRepository.save(sampleTask1); // oldest
        taskRepository.save(sampleTask2); // middle
        taskRepository.save(sampleTask3); // newest

        // Act
        List<Task> tasks = taskRepository.findAll();

        // Assert
        assertThat(tasks).hasSize(3);
        assertThat(tasks.get(0)).isEqualTo(sampleTask3); // newest first
        assertThat(tasks.get(1)).isEqualTo(sampleTask2);
        assertThat(tasks.get(2)).isEqualTo(sampleTask1); // oldest last
    }

    @Test
    @DisplayName("findAll: should return list as immutable/unmodifiable")
    void findAll_ShouldReturnUnmodifiableList() {
        // Arrange
        taskRepository.save(sampleTask1);

        // Act
        List<Task> tasks = taskRepository.findAll();

        // Assert - Creating new list should work but modifying returned list should fail
        assertThat(tasks).isNotNull();
        assertThatThrownBy(() -> tasks.add(sampleTask2))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("findAll: should return correct order with multiple inserts")
    void findAll_ShouldMaintainCorrectOrderWithMultipleInserts() {
        // Arrange - Create tasks with controlled timestamps
        Task oldTask = Task.builder().id("old").createdAt(LocalDateTime.now().minusHours(10)).build();
        Task middleTask = Task.builder().id("middle").createdAt(LocalDateTime.now().minusHours(5)).build();
        Task newTask = Task.builder().id("new").createdAt(LocalDateTime.now()).build();

        taskRepository.save(oldTask);
        taskRepository.save(newTask);
        taskRepository.save(middleTask);

        // Act
        List<Task> tasks = taskRepository.findAll();

        // Assert
        assertThat(tasks.get(0).getId()).isEqualTo("new");
        assertThat(tasks.get(1).getId()).isEqualTo("middle");
        assertThat(tasks.get(2).getId()).isEqualTo("old");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ────────────── DELETE BY ID METHOD TESTS ────────────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("deleteById: should delete existing task")
    void deleteById_ShouldDeleteExistingTask() {
        // Arrange
        taskRepository.save(sampleTask1);
        assertThat(taskRepository.count()).isEqualTo(1);

        // Act
        boolean deleted = taskRepository.deleteById("task-1");

        // Assert
        assertThat(deleted).isTrue();
        assertThat(taskRepository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("deleteById: should return false for non-existent task")
    void deleteById_ShouldReturnFalseForNonExistent() {
        // Act
        boolean deleted = taskRepository.deleteById("non-existent");

        // Assert
        assertThat(deleted).isFalse();
    }

    @Test
    @DisplayName("deleteById: should not delete other tasks")
    void deleteById_ShouldNotDeleteOtherTasks() {
        // Arrange
        taskRepository.save(sampleTask1);
        taskRepository.save(sampleTask2);
        taskRepository.save(sampleTask3);

        // Act
        taskRepository.deleteById("task-1");

        // Assert
        assertThat(taskRepository.count()).isEqualTo(2);
        assertThat(taskRepository.findById("task-2")).isPresent();
        assertThat(taskRepository.findById("task-3")).isPresent();
        assertThat(taskRepository.findById("task-1")).isEmpty();
    }

    @Test
    @DisplayName("deleteById: should remove task from findAll results")
    void deleteById_ShouldRemoveTaskFromFindAll() {
        // Arrange
        taskRepository.save(sampleTask1);
        taskRepository.save(sampleTask2);

        // Act
        taskRepository.deleteById("task-1");
        List<Task> tasks = taskRepository.findAll();

        // Assert
        assertThat(tasks).hasSize(1);
        assertThat(tasks).contains(sampleTask2);
        assertThat(tasks).doesNotContain(sampleTask1);
    }

    @Test
    @DisplayName("deleteById: should be idempotent (delete again returns false)")
    void deleteById_ShouldBeIdempotent() {
        // Arrange
        taskRepository.save(sampleTask1);

        // Act
        boolean firstDelete = taskRepository.deleteById("task-1");
        boolean secondDelete = taskRepository.deleteById("task-1");

        // Assert
        assertThat(firstDelete).isTrue();
        assertThat(secondDelete).isFalse();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ────────────── EXISTS BY ID METHOD TESTS ────────────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("existsById: should return true for existing task")
    void existsById_ShouldReturnTrueForExistingTask() {
        // Arrange
        taskRepository.save(sampleTask1);

        // Act
        boolean exists = taskRepository.existsById("task-1");

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("existsById: should return false for non-existent task")
    void existsById_ShouldReturnFalseForNonExistent() {
        // Act
        boolean exists = taskRepository.existsById("non-existent");

        // Assert
        assertThat(exists).isFalse();
    }


    @Test
    @DisplayName("existsById: should return false after task deletion")
    void existsById_ShouldReturnFalseAfterDeletion() {
        // Arrange
        taskRepository.save(sampleTask1);
        assertThat(taskRepository.existsById("task-1")).isTrue();

        // Act
        taskRepository.deleteById("task-1");

        // Assert
        assertThat(taskRepository.existsById("task-1")).isFalse();
    }

    @Test
    @DisplayName("existsById: should be case-sensitive")
    void existsById_ShouldBeCaseSensitive() {
        // Arrange
        taskRepository.save(sampleTask1);

        // Act & Assert
        assertThat(taskRepository.existsById("task-1")).isTrue();
        assertThat(taskRepository.existsById("TASK-1")).isFalse();
        assertThat(taskRepository.existsById("Task-1")).isFalse();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ────────────── COUNT METHOD TESTS ──────────────────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("count: should return 0 for empty repository")
    void count_ShouldReturnZeroForEmptyRepository() {
        // Act
        long count = taskRepository.count();

        // Assert
        assertThat(count).isEqualTo(0);
    }

    @Test
    @DisplayName("count: should return correct count after saving tasks")
    void count_ShouldReturnCorrectCount() {
        // Arrange
        taskRepository.save(sampleTask1);
        taskRepository.save(sampleTask2);
        taskRepository.save(sampleTask3);

        // Act
        long count = taskRepository.count();

        // Assert
        assertThat(count).isEqualTo(3);
    }

    @Test
    @DisplayName("count: should increment after each save")
    void count_ShouldIncrementAfterEachSave() {
        // Act & Assert
        assertThat(taskRepository.count()).isEqualTo(0);

        taskRepository.save(sampleTask1);
        assertThat(taskRepository.count()).isEqualTo(1);

        taskRepository.save(sampleTask2);
        assertThat(taskRepository.count()).isEqualTo(2);

        taskRepository.save(sampleTask3);
        assertThat(taskRepository.count()).isEqualTo(3);
    }

    @Test
    @DisplayName("count: should decrement after deletion")
    void count_ShouldDecrementAfterDeletion() {
        // Arrange
        taskRepository.save(sampleTask1);
        taskRepository.save(sampleTask2);
        taskRepository.save(sampleTask3);
        assertThat(taskRepository.count()).isEqualTo(3);

        // Act & Assert
        taskRepository.deleteById("task-1");
        assertThat(taskRepository.count()).isEqualTo(2);

        taskRepository.deleteById("task-2");
        assertThat(taskRepository.count()).isEqualTo(1);

        taskRepository.deleteById("task-3");
        assertThat(taskRepository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("count: should not change when updating existing task")
    void count_ShouldNotChangeWhenUpdatingExistingTask() {
        // Arrange
        taskRepository.save(sampleTask1);
        long countBefore = taskRepository.count();

        // Act
        sampleTask1.setTitle("Updated Title");
        taskRepository.save(sampleTask1);
        long countAfter = taskRepository.count();

        // Assert
        assertThat(countBefore).isEqualTo(countAfter).isEqualTo(1);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ────────────── CONCURRENT HASH MAP THREAD SAFETY TESTS ──────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("repository: should maintain data integrity with concurrent operations")
    void repository_ShouldMaintainDataIntegrityWithConcurrentOperations() throws InterruptedException {
        // Arrange
        int taskCount = 100;
        List<Thread> threads = new ArrayList<>();

        // Act - Create multiple threads that save tasks
        for (int i = 0; i < taskCount; i++) {
            final int index = i;
            Thread thread = new Thread(() -> {
                Task task = Task.builder()
                        .id("task-" + index)
                        .title("Task " + index)
                        .build();
                taskRepository.save(task);
            });
            threads.add(thread);
            thread.start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Assert
        assertThat(taskRepository.count()).isEqualTo(taskCount);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ────────────── WORKFLOW INTEGRATION TESTS ──────────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("workflow: complete CRUD cycle")
    void workflow_CompleteCRUDCycle() {
        // Create
        taskRepository.save(sampleTask1);
        assertThat(taskRepository.count()).isEqualTo(1);

        // Read
        Optional<Task> foundTask = taskRepository.findById("task-1");
        assertThat(foundTask).isPresent();
        assertThat(foundTask.get().getTitle()).isEqualTo("Complete Project");

        // Update
        sampleTask1.setTitle("Updated Project");
        taskRepository.save(sampleTask1);
        Optional<Task> updatedTask = taskRepository.findById("task-1");
        assertThat(updatedTask.get().getTitle()).isEqualTo("Updated Project");

        // Delete
        boolean deleted = taskRepository.deleteById("task-1");
        assertThat(deleted).isTrue();
        assertThat(taskRepository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("workflow: multiple tasks creation and management")
    void workflow_MultipleTasksCreationAndManagement() {
        // Create multiple tasks
        taskRepository.save(sampleTask1);
        taskRepository.save(sampleTask2);
        taskRepository.save(sampleTask3);

        // Verify all exist
        assertThat(taskRepository.count()).isEqualTo(3);
        assertThat(taskRepository.existsById("task-1")).isTrue();
        assertThat(taskRepository.existsById("task-2")).isTrue();
        assertThat(taskRepository.existsById("task-3")).isTrue();

        // Find all and verify sorting
        List<Task> allTasks = taskRepository.findAll();
        assertThat(allTasks).hasSize(3);

        // Update one task
        sampleTask2.setStatus(TaskStatus.COMPLETED);
        taskRepository.save(sampleTask2);

        // Delete one task
        taskRepository.deleteById("task-1");
        assertThat(taskRepository.count()).isEqualTo(2);

        // Verify remaining tasks
        List<Task> remainingTasks = taskRepository.findAll();
        assertThat(remainingTasks).hasSize(2);
        assertThat(remainingTasks).doesNotContain(sampleTask1);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ────────────── EDGE CASES & VALIDATION TESTS ────────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("repository: should handle tasks with empty strings")
    void repository_ShouldHandleEmptyStrings() {
        // Arrange
        Task emptyTask = Task.builder()
                .id("empty")
                .title("")
                .description("")
                .build();

        // Act
        taskRepository.save(emptyTask);

        // Assert
        Optional<Task> found = taskRepository.findById("empty");
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEmpty();
        assertThat(found.get().getDescription()).isEmpty();
    }

    @Test
    @DisplayName("repository: should handle tasks with very long ids")
    void repository_ShouldHandleVeryLongIds() {
        // Arrange
        String longId = "task-".repeat(50); // Very long id
        Task taskWithLongId = Task.builder()
                .id(longId)
                .title("Task with long id")
                .build();

        // Act
        taskRepository.save(taskWithLongId);

        // Assert
        Optional<Task> found = taskRepository.findById(longId);
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(longId);
    }

    @Test
    @DisplayName("repository: should handle special characters in task ids")
    void repository_ShouldHandleSpecialCharactersInIds() {
        // Arrange
        String specialId = "task-@#$%^&*()";
        Task taskWithSpecialId = Task.builder()
                .id(specialId)
                .title("Task with special id")
                .build();

        // Act
        taskRepository.save(taskWithSpecialId);

        // Assert
        Optional<Task> found = taskRepository.findById(specialId);
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(specialId);
    }

    @Test
    @DisplayName("repository: should handle finding task with empty string id")
    void repository_ShouldHandleFindingEmptyStringId() {
        // Act
        Optional<Task> found = taskRepository.findById("");

        // Assert
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("repository: should preserve task state after retrieval")
    void repository_ShouldPreserveTaskStateAfterRetrieval() {
        // Arrange
        taskRepository.save(sampleTask1);

        // Act
        Optional<Task> foundTask1 = taskRepository.findById("task-1");
        Optional<Task> foundTask2 = taskRepository.findById("task-1");

        // Assert
        assertThat(foundTask1).isPresent();
        assertThat(foundTask2).isPresent();
        assertThat(foundTask1.get()).isEqualTo(foundTask2.get());
    }

    @Test
    @DisplayName("repository: should return the same object instance on repeated saves")
    void repository_ShouldReturnSameInstanceOnRepeatedSaves() {
        // Act
        Task saved1 = taskRepository.save(sampleTask1);
        Task saved2 = taskRepository.save(sampleTask1);

        // Assert
        assertThat(saved1).isSameAs(saved2).isSameAs(sampleTask1);
    }
}

