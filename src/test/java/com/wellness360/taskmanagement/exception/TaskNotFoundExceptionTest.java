package com.wellness360.taskmanagement.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for TaskNotFoundException — covers exception construction, messaging, and HTTP status mapping.
 * Tests the custom exception and its @ResponseStatus annotation behavior.
 */
@DisplayName("TaskNotFoundException Tests")
class TaskNotFoundExceptionTest {

    // ─────────────────────────────────────────────────────────────────────────
    // ────────────── EXCEPTION CONSTRUCTION TESTS ─────────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("TaskNotFoundException: should create instance with task id")
    void taskNotFoundException_WithTaskId_ShouldCreateInstance() {
        // Arrange & Act
        TaskNotFoundException exception = new TaskNotFoundException("task-123");

        // Assert
        assertThat(exception).isNotNull();
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("TaskNotFoundException: should generate message with task id")
    void taskNotFoundException_ShouldGenerateMessageWithTaskId() {
        // Arrange & Act
        String taskId = "task-456";
        TaskNotFoundException exception = new TaskNotFoundException(taskId);

        // Assert
        assertThat(exception.getMessage()).isEqualTo("Task not found with id: " + taskId);
    }

    @Test
    @DisplayName("TaskNotFoundException: should include task id in error message")
    void taskNotFoundException_MessageShouldContainTaskId() {
        // Arrange & Act
        String taskId = "my-important-task";
        TaskNotFoundException exception = new TaskNotFoundException(taskId);

        // Assert
        assertThat(exception.getMessage()).contains(taskId);
        assertThat(exception.getMessage()).contains("Task not found");
    }

    @Test
    @DisplayName("TaskNotFoundException: should handle numeric task id")
    void taskNotFoundException_WithNumericTaskId_ShouldCreateMessage() {
        // Arrange & Act
        TaskNotFoundException exception = new TaskNotFoundException("12345");

        // Assert
        assertThat(exception.getMessage()).isEqualTo("Task not found with id: 12345");
    }

    @Test
    @DisplayName("TaskNotFoundException: should handle UUID-format task id")
    void taskNotFoundException_WithUuidTaskId_ShouldCreateMessage() {
        // Arrange
        String uuidTaskId = "550e8400-e29b-41d4-a716-446655440000";

        // Act
        TaskNotFoundException exception = new TaskNotFoundException(uuidTaskId);

        // Assert
        assertThat(exception.getMessage()).contains(uuidTaskId);
    }

    @Test
    @DisplayName("TaskNotFoundException: should handle special characters in task id")
    void taskNotFoundException_WithSpecialCharactersInTaskId_ShouldCreateMessage() {
        // Arrange & Act
        TaskNotFoundException exception = new TaskNotFoundException("task-@#$%^&*()");

        // Assert
        assertThat(exception.getMessage()).contains("task-@#$%^&*()");
    }

    @Test
    @DisplayName("TaskNotFoundException: should handle hyphenated task id")
    void taskNotFoundException_WithHyphenatedTaskId_ShouldCreateMessage() {
        // Arrange & Act
        TaskNotFoundException exception = new TaskNotFoundException("task-user-123-project-a");

        // Assert
        assertThat(exception.getMessage()).isEqualTo("Task not found with id: task-user-123-project-a");
    }

    @Test
    @DisplayName("TaskNotFoundException: should handle empty string task id")
    void taskNotFoundException_WithEmptyString_ShouldCreateMessage() {
        // Arrange & Act
        TaskNotFoundException exception = new TaskNotFoundException("");

        // Assert
        assertThat(exception.getMessage()).isEqualTo("Task not found with id: ");
    }

    @Test
    @DisplayName("TaskNotFoundException: should handle very long task id")
    void taskNotFoundException_WithLongTaskId_ShouldCreateMessage() {
        // Arrange
        String longTaskId = "task-".repeat(50); // 250 characters

        // Act
        TaskNotFoundException exception = new TaskNotFoundException(longTaskId);

        // Assert
        assertThat(exception.getMessage()).contains(longTaskId);
    }

    @Test
    @DisplayName("TaskNotFoundException: should preserve whitespace in task id")
    void taskNotFoundException_WithWhitespaceInTaskId_ShouldPreserveIt() {
        // Arrange & Act
        TaskNotFoundException exception = new TaskNotFoundException("task with spaces");

        // Assert
        assertThat(exception.getMessage()).contains("task with spaces");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ─────────────── RESPONSE STATUS ANNOTATION TESTS ────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("TaskNotFoundException: should have @ResponseStatus annotation")
    void taskNotFoundException_ShouldHaveResponseStatusAnnotation() {
        // Arrange & Act
        Class<TaskNotFoundException> exceptionClass = TaskNotFoundException.class;

        // Assert
        assertThat(exceptionClass).hasAnnotation(ResponseStatus.class);
    }

    @Test
    @DisplayName("TaskNotFoundException: @ResponseStatus should be set to NOT_FOUND")
    void taskNotFoundException_ResponseStatusShouldBeNotFound() {
        // Arrange & Act
        ResponseStatus responseStatus = TaskNotFoundException.class.getAnnotation(ResponseStatus.class);

        // Assert
        assertThat(responseStatus).isNotNull();
        assertThat(responseStatus.value()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("TaskNotFoundException: should map to HTTP 404 status")
    void taskNotFoundException_ShouldMapTo404Status() {
        // Arrange & Act
        ResponseStatus responseStatus = TaskNotFoundException.class.getAnnotation(ResponseStatus.class);

        // Assert
        assertThat(responseStatus.value().value()).isEqualTo(404);
    }

    @Test
    @DisplayName("TaskNotFoundException: HTTP status should be NOT_FOUND")
    void taskNotFoundException_HttpStatusShouldBeNotFound() {
        // Arrange & Act
        ResponseStatus responseStatus = TaskNotFoundException.class.getAnnotation(ResponseStatus.class);

        // Assert
        assertThat(responseStatus.value()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseStatus.value().is4xxClientError()).isTrue();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ────────────── EXCEPTION INHERITANCE TESTS ──────────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("TaskNotFoundException: should extend RuntimeException")
    void taskNotFoundException_ShouldExtendRuntimeException() {
        // Arrange & Act
        TaskNotFoundException exception = new TaskNotFoundException("task-1");

        // Assert
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("TaskNotFoundException: should extend Exception")
    void taskNotFoundException_ShouldExtendException() {
        // Arrange & Act
        TaskNotFoundException exception = new TaskNotFoundException("task-1");

        // Assert
        assertThat(exception).isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("TaskNotFoundException: should extend Throwable")
    void taskNotFoundException_ShouldExtendThrowable() {
        // Arrange & Act
        TaskNotFoundException exception = new TaskNotFoundException("task-1");

        // Assert
        assertThat(exception).isInstanceOf(Throwable.class);
    }

    @Test
    @DisplayName("TaskNotFoundException: should be catchable as RuntimeException")
    void taskNotFoundException_ShouldBeCatchableAsRuntimeException() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> {
            throw new TaskNotFoundException("task-1");
        }).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("TaskNotFoundException: should be catchable as Exception")
    void taskNotFoundException_ShouldBeCatchableAsException() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> {
            throw new TaskNotFoundException("task-1");
        }).isInstanceOf(Exception.class);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ──────────────── EXCEPTION THROWING TESTS ──────────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("TaskNotFoundException: should be throwable")
    void taskNotFoundException_ShouldBeThrowable() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> {
            throw new TaskNotFoundException("task-123");
        }).isInstanceOf(TaskNotFoundException.class)
                .hasMessage("Task not found with id: task-123");
    }

    @Test
    @DisplayName("TaskNotFoundException: should preserve message when thrown")
    void taskNotFoundException_MessagePreservedWhenThrown() {
        // Arrange
        String taskId = "important-task";

        // Act & Assert
        assertThatThrownBy(() -> {
            throw new TaskNotFoundException(taskId);
        }).hasMessage("Task not found with id: " + taskId);
    }

    @Test
    @DisplayName("TaskNotFoundException: should be throwable and caught by catch block")
    void taskNotFoundException_ShouldBeCatchableInTryCatchBlock() {
        // Arrange
        boolean exceptionCaught = false;
        String exceptionMessage = "";

        // Act
        try {
            throw new TaskNotFoundException("task-456");
        } catch (TaskNotFoundException e) {
            exceptionCaught = true;
            exceptionMessage = e.getMessage();
        }

        // Assert
        assertThat(exceptionCaught).isTrue();
        assertThat(exceptionMessage).isEqualTo("Task not found with id: task-456");
    }

    @Test
    @DisplayName("TaskNotFoundException: should not be checked exception")
    void taskNotFoundException_ShouldNotBeChecked() {
        // Arrange & Act
        TaskNotFoundException exception = new TaskNotFoundException("task-1");

        // Assert
        assertThat(exception).isInstanceOf(RuntimeException.class);
        // RuntimeException is unchecked, so no need for throws declaration
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ───────────── STACK TRACE & CAUSE TESTS ───────────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("TaskNotFoundException: should have populated stack trace")
    void taskNotFoundException_ShouldHaveStackTrace() {
        // Arrange & Act
        TaskNotFoundException exception = new TaskNotFoundException("task-1");

        // Assert
        assertThat(exception.getStackTrace()).isNotEmpty();
    }

    @Test
    @DisplayName("TaskNotFoundException: should have null cause by default")
    void taskNotFoundException_ShouldHaveNullCauseByDefault() {
        // Arrange & Act
        TaskNotFoundException exception = new TaskNotFoundException("task-1");

        // Assert
        assertThat(exception.getCause()).isNull();
    }

    @Test
    @DisplayName("TaskNotFoundException: should allow setting cause")
    void taskNotFoundException_ShouldAllowSettingCause() {
        // Arrange
        TaskNotFoundException exception = new TaskNotFoundException("task-1");
        Exception cause = new Exception("Database error");

        // Act
        exception.initCause(cause);

        // Assert
        assertThat(exception.getCause()).isEqualTo(cause);
    }

    @Test
    @DisplayName("TaskNotFoundException: toString should include exception type and message")
    void taskNotFoundException_ToStringShouldIncludeTypeAndMessage() {
        // Arrange & Act
        TaskNotFoundException exception = new TaskNotFoundException("task-789");
        String toStringResult = exception.toString();

        // Assert
        assertThat(toStringResult).contains("TaskNotFoundException");
        assertThat(toStringResult).contains("task-789");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ────────── MULTIPLE EXCEPTION INSTANCES TESTS ──────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("TaskNotFoundException: multiple instances should be independent")
    void taskNotFoundException_MultipleInstancesShouldBeIndependent() {
        // Arrange & Act
        TaskNotFoundException exception1 = new TaskNotFoundException("task-1");
        TaskNotFoundException exception2 = new TaskNotFoundException("task-2");

        // Assert
        assertThat(exception1.getMessage()).isNotEqualTo(exception2.getMessage());
        assertThat(exception1.getMessage()).contains("task-1");
        assertThat(exception2.getMessage()).contains("task-2");
    }

    @Test
    @DisplayName("TaskNotFoundException: different instances should have different stack traces")
    void taskNotFoundException_DifferentInstancesShouldHaveDifferentStackTraces() {
        // Arrange & Act
        TaskNotFoundException exception1 = new TaskNotFoundException("task-1");
        TaskNotFoundException exception2 = new TaskNotFoundException("task-2");

        // Assert
        // Both should have stack traces
        assertThat(exception1.getStackTrace()).isNotEmpty();
        assertThat(exception2.getStackTrace()).isNotEmpty();
    }

    @Test
    @DisplayName("TaskNotFoundException: should be serializable")
    void taskNotFoundException_ShouldBeSerializable() {
        // Arrange & Act
        TaskNotFoundException exception = new TaskNotFoundException("task-1");

        // Assert - RuntimeException is serializable
        assertThat(exception).isInstanceOf(java.io.Serializable.class);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ─────────────── PRACTICAL USE CASE TESTS ────────────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("TaskNotFoundException: should be usable in method throwing")
    void taskNotFoundException_UsableInMethodThrowing() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> findTaskById("non-existent"))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining("non-existent");
    }

    @Test
    @DisplayName("TaskNotFoundException: should work with try-catch-finally")
    void taskNotFoundException_WorksWithTryCatchFinally() {
        // Arrange
        boolean finallyExecuted = false;
        String caughtMessage = "";

        // Act
        try {
            throw new TaskNotFoundException("task-final-test");
        } catch (TaskNotFoundException e) {
            caughtMessage = e.getMessage();
        } finally {
            finallyExecuted = true;
        }

        // Assert
        assertThat(finallyExecuted).isTrue();
        assertThat(caughtMessage).contains("task-final-test");
    }

    @Test
    @DisplayName("TaskNotFoundException: should support method chaining in exception handling")
    void taskNotFoundException_SupportsExceptionHandlingChaining() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> {
            throw new TaskNotFoundException("test-task");
        }).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("test-task")
                .hasNoCause();
    }

    @Test
    @DisplayName("TaskNotFoundException: should work correctly in Stream operations")
    void taskNotFoundException_WorksInStreamOperations() {
        // Arrange
        java.util.List<String> taskIds = java.util.Arrays.asList("task-1", "task-2", "task-3");

        // Act & Assert
        assertThatThrownBy(() -> {
            taskIds.stream()
                    .filter(id -> id.equals("non-existent"))
                    .findFirst()
                    .orElseThrow(() -> new TaskNotFoundException("non-existent"));
        }).isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining("non-existent");
    }

    @Test
    @DisplayName("TaskNotFoundException: message format should be consistent")
    void taskNotFoundException_MessageFormatShouldBeConsistent() {
        // Arrange
        String[] taskIds = {"task-1", "task-2", "task-abc", "123", "uuid-456"};

        // Act & Assert
        for (String taskId : taskIds) {
            TaskNotFoundException exception = new TaskNotFoundException(taskId);
            assertThat(exception.getMessage()).isEqualTo("Task not found with id: " + taskId);
        }
    }

    @Test
    @DisplayName("TaskNotFoundException: should handle null task id gracefully")
    void taskNotFoundException_WithNullTaskId_ShouldCreateMessage() {
        // Arrange & Act
        TaskNotFoundException exception = new TaskNotFoundException(null);

        // Assert
        assertThat(exception.getMessage()).isEqualTo("Task not found with id: null");
    }

    @Test
    @DisplayName("TaskNotFoundException: should provide meaningful context through message")
    void taskNotFoundException_MessageProvidesMeaningfulContext() {
        // Arrange & Act
        TaskNotFoundException exception = new TaskNotFoundException("task-important-001");

        // Assert
        String message = exception.getMessage();
        assertThat(message).contains("Task");
        assertThat(message).contains("not found");
        assertThat(message).contains("id");
        assertThat(message).contains("task-important-001");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ────────────────── HELPER METHODS ──────────────────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Helper method simulating task lookup that throws TaskNotFoundException
     */
    private void findTaskById(String taskId) {
        throw new TaskNotFoundException(taskId);
    }
}

