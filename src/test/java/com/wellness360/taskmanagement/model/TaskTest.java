package com.wellness360.taskmanagement.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for Task entity — covers constructors, factory methods, and business logic.
 * Tests entity creation, state management, timestamps, and the touch() method.
 */
@DisplayName("Task Entity Tests")
class TaskTest {

    private Task task;
    private LocalDateTime baseTime;

    @BeforeEach
    void setUp() {
        baseTime = LocalDateTime.now();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ──────────────── NO-ARG CONSTRUCTOR TESTS ──────────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Task: should create instance with no-arg constructor")
    void task_NoArgConstructor_ShouldCreateInstance() {
        // Arrange & Act
        task = new Task();

        // Assert
        assertThat(task).isNotNull();
    }

    @Test
    @DisplayName("Task: no-arg constructor should set all fields to null")
    void task_NoArgConstructor_ShouldSetFieldsToNull() {
        // Arrange & Act
        task = new Task();

        // Assert
        assertThat(task.getId()).isNull();
        assertThat(task.getTitle()).isNull();
        assertThat(task.getDescription()).isNull();
        assertThat(task.getDueDate()).isNull();
        assertThat(task.getStatus()).isNull();
        assertThat(task.getCreatedAt()).isNull();
        assertThat(task.getUpdatedAt()).isNull();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ────────────── ALL-ARG CONSTRUCTOR TESTS ────────────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Task: should create instance with all-arg constructor")
    void task_AllArgConstructor_ShouldCreateInstance() {
        // Arrange & Act
        LocalDate dueDate = LocalDate.now().plusDays(5);
        task = new Task("task-1", "Title", "Description", dueDate, TaskStatus.PENDING, baseTime, baseTime);

        // Assert
        assertThat(task).isNotNull();
        assertThat(task.getId()).isEqualTo("task-1");
        assertThat(task.getTitle()).isEqualTo("Title");
    }

    @Test
    @DisplayName("Task: all-arg constructor should set all fields correctly")
    void task_AllArgConstructor_ShouldSetAllFields() {
        // Arrange
        LocalDate dueDate = LocalDate.now().plusDays(7);
        LocalDateTime createdAt = LocalDateTime.now().minusHours(2);
        LocalDateTime updatedAt = LocalDateTime.now();

        // Act
        task = new Task("task-123", "Complete Project", "Finish Spring Boot", dueDate, TaskStatus.IN_PROGRESS, createdAt, updatedAt);

        // Assert
        assertThat(task.getId()).isEqualTo("task-123");
        assertThat(task.getTitle()).isEqualTo("Complete Project");
        assertThat(task.getDescription()).isEqualTo("Finish Spring Boot");
        assertThat(task.getDueDate()).isEqualTo(dueDate);
        assertThat(task.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
        assertThat(task.getCreatedAt()).isEqualTo(createdAt);
        assertThat(task.getUpdatedAt()).isEqualTo(updatedAt);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ────────────── FACTORY METHOD (CREATE) TESTS ────────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Task.create: should generate new task with valid data")
    void task_CreateFactory_ShouldCreateNewTask() {
        // Arrange & Act
        LocalDate dueDate = LocalDate.now().plusDays(5);
        task = Task.create("New Task", "Task Description", dueDate);

        // Assert
        assertThat(task).isNotNull();
        assertThat(task.getTitle()).isEqualTo("New Task");
        assertThat(task.getDescription()).isEqualTo("Task Description");
        assertThat(task.getDueDate()).isEqualTo(dueDate);
    }

    @Test
    @DisplayName("Task.create: should generate UUID for task id")
    void task_CreateFactory_ShouldGenerateUuidId() {
        // Arrange & Act
        task = Task.create("Task", "Description", LocalDate.now().plusDays(3));

        // Assert
        assertThat(task.getId()).isNotNull();
        assertThat(task.getId()).isNotEmpty();
        // Verify it's a valid UUID format
        assertThatNoException().isThrownBy(() -> UUID.fromString(task.getId()));
    }

    @Test
    @DisplayName("Task.create: should set status to PENDING")
    void task_CreateFactory_ShouldSetStatusPending() {
        // Arrange & Act
        task = Task.create("Task", "Description", LocalDate.now());

        // Assert
        assertThat(task.getStatus()).isEqualTo(TaskStatus.PENDING);
    }

    @Test
    @DisplayName("Task.create: should set createdAt timestamp")
    void task_CreateFactory_ShouldSetCreatedAtTimestamp() {
        // Arrange & Act
        LocalDateTime beforeCreation = LocalDateTime.now();
        task = Task.create("Task", "Description", LocalDate.now());
        LocalDateTime afterCreation = LocalDateTime.now();

        // Assert
        assertThat(task.getCreatedAt()).isNotNull();
        assertThat(task.getCreatedAt()).isAfterOrEqualTo(beforeCreation);
        assertThat(task.getCreatedAt()).isBeforeOrEqualTo(afterCreation);
    }

    @Test
    @DisplayName("Task.create: should set updatedAt timestamp equal to createdAt")
    void task_CreateFactory_ShouldSetUpdatedAtEqualToCreatedAt() {
        // Arrange & Act
        task = Task.create("Task", "Description", LocalDate.now());

        // Assert
        assertThat(task.getUpdatedAt()).isEqualTo(task.getCreatedAt());
    }

    @Test
    @DisplayName("Task.create: each call should generate different UUIDs")
    void task_CreateFactory_EachCallShouldGenerateUniqueId() {
        // Arrange & Act
        Task task1 = Task.create("Task 1", "Description 1", LocalDate.now());
        Task task2 = Task.create("Task 2", "Description 2", LocalDate.now());
        Task task3 = Task.create("Task 3", "Description 3", LocalDate.now());

        // Assert
        assertThat(task1.getId()).isNotEqualTo(task2.getId());
        assertThat(task2.getId()).isNotEqualTo(task3.getId());
        assertThat(task1.getId()).isNotEqualTo(task3.getId());
    }

    @Test
    @DisplayName("Task.create: should accept null description")
    void task_CreateFactory_ShouldAcceptNullDescription() {
        // Arrange & Act
        task = Task.create("Task Title", null, LocalDate.now().plusDays(5));

        // Assert
        assertThat(task.getTitle()).isEqualTo("Task Title");
        assertThat(task.getDescription()).isNull();
    }

    @Test
    @DisplayName("Task.create: should preserve all parameters")
    void task_CreateFactory_ShouldPreserveAllParameters() {
        // Arrange
        String title = "Test Task";
        String description = "Test Description";
        LocalDate dueDate = LocalDate.of(2025, 12, 31);

        // Act
        task = Task.create(title, description, dueDate);

        // Assert
        assertThat(task.getTitle()).isEqualTo(title);
        assertThat(task.getDescription()).isEqualTo(description);
        assertThat(task.getDueDate()).isEqualTo(dueDate);
    }

    @Test
    @DisplayName("Task.create: should handle special characters in title")
    void task_CreateFactory_WithSpecialCharactersInTitle() {
        // Arrange & Act
        task = Task.create("Task: Complete & Review @50% #Urgent!", "Description", LocalDate.now());

        // Assert
        assertThat(task.getTitle()).isEqualTo("Task: Complete & Review @50% #Urgent!");
    }

    @Test
    @DisplayName("Task.create: should handle special characters in description")
    void task_CreateFactory_WithSpecialCharactersInDescription() {
        // Arrange & Act
        task = Task.create("Task", "Desc with @#$%^&*()", LocalDate.now());

        // Assert
        assertThat(task.getDescription()).isEqualTo("Desc with @#$%^&*()");
    }

    @Test
    @DisplayName("Task.create: should handle very long title")
    void task_CreateFactory_WithVeryLongTitle() {
        // Arrange
        String longTitle = "A".repeat(500);

        // Act
        task = Task.create(longTitle, "Description", LocalDate.now());

        // Assert
        assertThat(task.getTitle()).isEqualTo(longTitle);
        assertThat(task.getTitle()).hasSize(500);
    }

    @Test
    @DisplayName("Task.create: should handle today's date as due date")
    void task_CreateFactory_WithTodayAsDueDate() {
        // Arrange
        LocalDate today = LocalDate.now();

        // Act
        task = Task.create("Task", "Description", today);

        // Assert
        assertThat(task.getDueDate()).isEqualTo(today);
    }

    @Test
    @DisplayName("Task.create: should handle future date as due date")
    void task_CreateFactory_WithFutureDateAsDueDate() {
        // Arrange
        LocalDate futureDate = LocalDate.now().plusYears(1);

        // Act
        task = Task.create("Task", "Description", futureDate);

        // Assert
        assertThat(task.getDueDate()).isEqualTo(futureDate);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ──────────────── BUILDER PATTERN TESTS ──────────────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Task.builder: should create instance using builder")
    void task_Builder_ShouldCreateInstance() {
        // Arrange & Act
        task = Task.builder()
                .id("task-1")
                .title("Title")
                .description("Description")
                .dueDate(LocalDate.now())
                .status(TaskStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Assert
        assertThat(task).isNotNull();
        assertThat(task.getId()).isEqualTo("task-1");
    }

    @Test
    @DisplayName("Task.builder: should allow partial configuration")
    void task_Builder_ShouldAllowPartialConfiguration() {
        // Arrange & Act
        task = Task.builder()
                .id("task-1")
                .title("Title")
                .build();

        // Assert
        assertThat(task.getId()).isEqualTo("task-1");
        assertThat(task.getTitle()).isEqualTo("Title");
        assertThat(task.getDescription()).isNull();
        assertThat(task.getDueDate()).isNull();
        assertThat(task.getStatus()).isNull();
    }

    @Test
    @DisplayName("Task.builder: should build with all fields set")
    void task_Builder_ShouldBuildWithAllFields() {
        // Arrange
        LocalDate dueDate = LocalDate.now().plusDays(10);
        LocalDateTime now = LocalDateTime.now();

        // Act
        task = Task.builder()
                .id("task-123")
                .title("Complete Project")
                .description("Finish implementation")
                .dueDate(dueDate)
                .status(TaskStatus.IN_PROGRESS)
                .createdAt(now)
                .updatedAt(now)
                .build();

        // Assert
        assertThat(task.getId()).isEqualTo("task-123");
        assertThat(task.getTitle()).isEqualTo("Complete Project");
        assertThat(task.getDescription()).isEqualTo("Finish implementation");
        assertThat(task.getDueDate()).isEqualTo(dueDate);
        assertThat(task.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
        assertThat(task.getCreatedAt()).isEqualTo(now);
        assertThat(task.getUpdatedAt()).isEqualTo(now);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ──────────────── GETTER/SETTER TESTS ────────────────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Task: should set and get id")
    void task_SetAndGetId_ShouldWork() {
        // Arrange
        task = new Task();

        // Act
        task.setId("new-id");

        // Assert
        assertThat(task.getId()).isEqualTo("new-id");
    }

    @Test
    @DisplayName("Task: should set and get title")
    void task_SetAndGetTitle_ShouldWork() {
        // Arrange
        task = new Task();

        // Act
        task.setTitle("New Title");

        // Assert
        assertThat(task.getTitle()).isEqualTo("New Title");
    }

    @Test
    @DisplayName("Task: should set and get description")
    void task_SetAndGetDescription_ShouldWork() {
        // Arrange
        task = new Task();

        // Act
        task.setDescription("New Description");

        // Assert
        assertThat(task.getDescription()).isEqualTo("New Description");
    }

    @Test
    @DisplayName("Task: should set and get due date")
    void task_SetAndGetDueDate_ShouldWork() {
        // Arrange
        task = new Task();
        LocalDate dueDate = LocalDate.now().plusDays(5);

        // Act
        task.setDueDate(dueDate);

        // Assert
        assertThat(task.getDueDate()).isEqualTo(dueDate);
    }

    @Test
    @DisplayName("Task: should set and get status")
    void task_SetAndGetStatus_ShouldWork() {
        // Arrange
        task = new Task();

        // Act
        task.setStatus(TaskStatus.COMPLETED);

        // Assert
        assertThat(task.getStatus()).isEqualTo(TaskStatus.COMPLETED);
    }

    @Test
    @DisplayName("Task: should set and get createdAt")
    void task_SetAndGetCreatedAt_ShouldWork() {
        // Arrange
        task = new Task();
        LocalDateTime createdAt = LocalDateTime.now().minusHours(5);

        // Act
        task.setCreatedAt(createdAt);

        // Assert
        assertThat(task.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    @DisplayName("Task: should set and get updatedAt")
    void task_SetAndGetUpdatedAt_ShouldWork() {
        // Arrange
        task = new Task();
        LocalDateTime updatedAt = LocalDateTime.now();

        // Act
        task.setUpdatedAt(updatedAt);

        // Assert
        assertThat(task.getUpdatedAt()).isEqualTo(updatedAt);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ────────────── TOUCH METHOD TESTS ──────────────────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Task.touch: should update the updatedAt timestamp")
    void task_Touch_ShouldUpdateTimestamp() {
        // Arrange
        task = Task.create("Task", "Description", LocalDate.now());
        LocalDateTime originalUpdatedAt = task.getUpdatedAt();

        // Add small delay to ensure timestamp difference
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Act
        task.touch();

        // Assert
        assertThat(task.getUpdatedAt()).isAfter(originalUpdatedAt);
    }

    @Test
    @DisplayName("Task.touch: should not change createdAt timestamp")
    void task_Touch_ShouldNotChangeCreatedAt() {
        // Arrange
        task = Task.create("Task", "Description", LocalDate.now());
        LocalDateTime originalCreatedAt = task.getCreatedAt();

        // Act
        task.touch();

        // Assert
        assertThat(task.getCreatedAt()).isEqualTo(originalCreatedAt);
    }

    @Test
    @DisplayName("Task.touch: should set updatedAt to current time")
    void task_Touch_ShouldSetUpdatedAtToCurrent() {
        // Arrange
        task = Task.create("Task", "Description", LocalDate.now().plusDays(1));
        LocalDateTime beforeTouch = LocalDateTime.now();

        // Act
        task.touch();

        LocalDateTime afterTouch = LocalDateTime.now();

        // Assert
        assertThat(task.getUpdatedAt()).isAfterOrEqualTo(beforeTouch);
        assertThat(task.getUpdatedAt()).isBeforeOrEqualTo(afterTouch);
    }

    @Test
    @DisplayName("Task.touch: should update updatedAt on multiple calls")
    void task_Touch_ShouldUpdateMultipleTimes() {
        // Arrange
        task = Task.create("Task", "Description", LocalDate.now());
        LocalDateTime firstTouch = task.getUpdatedAt();

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Act - First touch
        task.touch();
        LocalDateTime secondTime = task.getUpdatedAt();

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Act - Second touch
        task.touch();
        LocalDateTime thirdTime = task.getUpdatedAt();

        // Assert
        assertThat(firstTouch).isBefore(secondTime);
        assertThat(secondTime).isBefore(thirdTime);
    }

    @Test
    @DisplayName("Task.touch: should not affect other fields")
    void task_Touch_ShouldNotAffectOtherFields() {
        // Arrange
        task = Task.create("Original Title", "Original Description", LocalDate.now().plusDays(5));
        String originalId = task.getId();
        String originalTitle = task.getTitle();
        String originalDescription = task.getDescription();
        LocalDate originalDueDate = task.getDueDate();
        TaskStatus originalStatus = task.getStatus();
        LocalDateTime originalCreatedAt = task.getCreatedAt();

        // Act
        task.touch();

        // Assert
        assertThat(task.getId()).isEqualTo(originalId);
        assertThat(task.getTitle()).isEqualTo(originalTitle);
        assertThat(task.getDescription()).isEqualTo(originalDescription);
        assertThat(task.getDueDate()).isEqualTo(originalDueDate);
        assertThat(task.getStatus()).isEqualTo(originalStatus);
        assertThat(task.getCreatedAt()).isEqualTo(originalCreatedAt);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ────────────── TASK STATUS TESTS ──────────────────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Task: should support all TaskStatus values")
    void task_ShouldSupportAllTaskStatuses() {
        // Arrange, Act & Assert
        for (TaskStatus status : TaskStatus.values()) {
            task = Task.builder()
                    .status(status)
                    .build();
            assertThat(task.getStatus()).isEqualTo(status);
        }
    }

    @Test
    @DisplayName("Task: should allow changing status")
    void task_ShouldAllowChangingStatus() {
        // Arrange
        task = Task.create("Task", "Description", LocalDate.now());
        assertThat(task.getStatus()).isEqualTo(TaskStatus.PENDING);

        // Act
        task.setStatus(TaskStatus.IN_PROGRESS);

        // Assert
        assertThat(task.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
    }

    @Test
    @DisplayName("Task: should allow transitioning to completed status")
    void task_ShouldAllowTransitioningToCompleted() {
        // Arrange
        task = Task.create("Task", "Description", LocalDate.now());

        // Act
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setStatus(TaskStatus.COMPLETED);

        // Assert
        assertThat(task.getStatus()).isEqualTo(TaskStatus.COMPLETED);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ────────────── LOMBOK ANNOTATION TESTS ────────────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Task: should be comparable with equals and hashCode")
    void task_ShouldHaveEqualsAndHashCode() {
        // Arrange
        Task task1 = Task.builder().id("task-1").title("Title").build();
        Task task2 = Task.builder().id("task-1").title("Title").build();

        // Assert
        assertThat(task1).isEqualTo(task2);
        assertThat(task1.hashCode()).isEqualTo(task2.hashCode());
    }

    @Test
    @DisplayName("Task: should have toString method")
    void task_ShouldHaveToStringMethod() {
        // Arrange & Act
        task = Task.builder()
                .id("task-1")
                .title("Title")
                .build();

        String toStringResult = task.toString();

        // Assert
        assertThat(toStringResult).isNotNull();
        assertThat(toStringResult).contains("Task");
    }

    @Test
    @DisplayName("Task: two different tasks should not be equal")
    void task_DifferentTasksShouldNotBeEqual() {
        // Arrange
        Task task1 = Task.builder().id("task-1").title("Title 1").build();
        Task task2 = Task.builder().id("task-2").title("Title 2").build();

        // Assert
        assertThat(task1).isNotEqualTo(task2);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ────────────── EDGE CASES & VALIDATION TESTS ────────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Task: should handle null title")
    void task_WithNullTitle_ShouldStore() {
        // Arrange & Act
        task = Task.builder()
                .id("task-1")
                .title(null)
                .build();

        // Assert
        assertThat(task.getTitle()).isNull();
    }

    @Test
    @DisplayName("Task: should handle null description")
    void task_WithNullDescription_ShouldStore() {
        // Arrange & Act
        task = Task.builder()
                .title("Title")
                .description(null)
                .build();

        // Assert
        assertThat(task.getDescription()).isNull();
    }

    @Test
    @DisplayName("Task: should handle null due date")
    void task_WithNullDueDate_ShouldStore() {
        // Arrange & Act
        task = Task.builder()
                .title("Title")
                .dueDate(null)
                .build();

        // Assert
        assertThat(task.getDueDate()).isNull();
    }

    @Test
    @DisplayName("Task: should handle empty string title")
    void task_WithEmptyStringTitle_ShouldStore() {
        // Arrange & Act
        task = Task.builder()
                .title("")
                .build();

        // Assert
        assertThat(task.getTitle()).isEmpty();
    }

    @Test
    @DisplayName("Task: should handle empty string description")
    void task_WithEmptyStringDescription_ShouldStore() {
        // Arrange & Act
        task = Task.builder()
                .description("")
                .build();

        // Assert
        assertThat(task.getDescription()).isEmpty();
    }

    @Test
    @DisplayName("Task: should handle whitespace in title")
    void task_WithWhitespaceInTitle_ShouldStore() {
        // Arrange & Act
        task = Task.builder()
                .title("  Title with spaces  ")
                .build();

        // Assert
        assertThat(task.getTitle()).isEqualTo("  Title with spaces  ");
    }

    @Test
    @DisplayName("Task: should preserve past due dates")
    void task_WithPastDueDate_ShouldStore() {
        // Arrange & Act
        LocalDate pastDate = LocalDate.now().minusDays(10);
        task = Task.builder()
                .dueDate(pastDate)
                .build();

        // Assert
        assertThat(task.getDueDate()).isEqualTo(pastDate);
    }

    @Test
    @DisplayName("Task: should support very distant future dates")
    void task_WithDistantFutureDate_ShouldStore() {
        // Arrange & Act
        LocalDate farFuture = LocalDate.of(2099, 12, 31);
        task = Task.builder()
                .dueDate(farFuture)
                .build();

        // Assert
        assertThat(task.getDueDate()).isEqualTo(farFuture);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ────────────── PRACTICAL USE CASE TESTS ────────────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Task: real-world creation and modification flow")
    void task_RealWorldCreationAndModificationFlow() {
        // Arrange - Create a task
        task = Task.create("Implement API", "Build REST endpoints", LocalDate.now().plusDays(7));
        String originalId = task.getId();
        LocalDateTime originalCreatedAt = task.getCreatedAt();

        // Assert initial state
        assertThat(task.getId()).isEqualTo(originalId);
        assertThat(task.getTitle()).isEqualTo("Implement API");
        assertThat(task.getStatus()).isEqualTo(TaskStatus.PENDING);
        assertThat(task.getCreatedAt()).isEqualTo(originalCreatedAt);

        // Act - Start working on it
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.touch();
        LocalDateTime touchTime = task.getUpdatedAt();

        // Assert after modification
        assertThat(task.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);

        // Act - Complete it
        task.setStatus(TaskStatus.COMPLETED);
        task.touch();

        // Assert final state
        assertThat(task.getStatus()).isEqualTo(TaskStatus.COMPLETED);
        assertThat(task.getId()).isEqualTo(originalId); // ID should never change
        assertThat(task.getCreatedAt()).isEqualTo(originalCreatedAt); // Created time should never change
    }

    @Test
    @DisplayName("Task: multiple tasks with different properties")
    void task_MultipleTasksWithDifferentProperties() {
        // Arrange & Act
        Task task1 = Task.create("Design Database", "Create schema", LocalDate.now().plusDays(3));
        Task task2 = Task.create("Write Tests", "Unit and integration tests", LocalDate.now().plusDays(5));
        Task task3 = Task.create("Deploy to Production", null, LocalDate.now().plusDays(10));

        // Assert
        assertThat(task1.getId()).isNotEqualTo(task2.getId()).isNotEqualTo(task3.getId());
        assertThat(task1.getStatus()).isEqualTo(task2.getStatus()).isEqualTo(task3.getStatus());
        assertThat(task3.getDescription()).isNull();
        assertThat(task1.getDescription()).isNotNull();
    }
}

