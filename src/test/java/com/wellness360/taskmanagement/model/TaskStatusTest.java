package com.wellness360.taskmanagement.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for TaskStatus enum — covers enum values, JSON serialization/deserialization,
 * and factory methods. Tests case-insensitivity and invalid value handling.
 */
@DisplayName("TaskStatus Enum Tests")
class TaskStatusTest {

    // ─────────────────────────────────────────────────────────────────────────
    // ────────────── ENUM VALUES TESTS ────────────────────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("TaskStatus: should have PENDING enum value")
    void taskStatus_ShouldHavePendingValue() {
        // Arrange & Act
        TaskStatus status = TaskStatus.PENDING;

        // Assert
        assertThat(status).isNotNull();
        assertThat(status).isEqualTo(TaskStatus.PENDING);
    }

    @Test
    @DisplayName("TaskStatus: should have IN_PROGRESS enum value")
    void taskStatus_ShouldHaveInProgressValue() {
        // Arrange & Act
        TaskStatus status = TaskStatus.IN_PROGRESS;

        // Assert
        assertThat(status).isNotNull();
        assertThat(status).isEqualTo(TaskStatus.IN_PROGRESS);
    }

    @Test
    @DisplayName("TaskStatus: should have COMPLETED enum value")
    void taskStatus_ShouldHaveCompletedValue() {
        // Arrange & Act
        TaskStatus status = TaskStatus.COMPLETED;

        // Assert
        assertThat(status).isNotNull();
        assertThat(status).isEqualTo(TaskStatus.COMPLETED);
    }

    @Test
    @DisplayName("TaskStatus: should have exactly three enum values")
    void taskStatus_ShouldHaveThreeValues() {
        // Arrange & Act
        TaskStatus[] values = TaskStatus.values();

        // Assert
        assertThat(values).hasSize(3);
        assertThat(values).contains(TaskStatus.PENDING, TaskStatus.IN_PROGRESS, TaskStatus.COMPLETED);
    }

    @Test
    @DisplayName("TaskStatus: all enum values should be distinct")
    void taskStatus_AllValuesShouldBeDistinct() {
        // Arrange & Act
        TaskStatus[] values = TaskStatus.values();

        // Assert
        assertThat(values[0]).isNotEqualTo(values[1]);
        assertThat(values[1]).isNotEqualTo(values[2]);
        assertThat(values[0]).isNotEqualTo(values[2]);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ────────────── VALUE GETTER TESTS ──────────────────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("TaskStatus.PENDING: getValue should return 'pending'")
    void taskStatus_Pending_GetValueShouldReturnPending() {
        // Arrange & Act
        TaskStatus status = TaskStatus.PENDING;
        String value = status.getValue();

        // Assert
        assertThat(value).isEqualTo("pending");
    }

    @Test
    @DisplayName("TaskStatus.IN_PROGRESS: getValue should return 'in_progress'")
    void taskStatus_InProgress_GetValueShouldReturnInProgress() {
        // Arrange & Act
        TaskStatus status = TaskStatus.IN_PROGRESS;
        String value = status.getValue();

        // Assert
        assertThat(value).isEqualTo("in_progress");
    }

    @Test
    @DisplayName("TaskStatus.COMPLETED: getValue should return 'completed'")
    void taskStatus_Completed_GetValueShouldReturnCompleted() {
        // Arrange & Act
        TaskStatus status = TaskStatus.COMPLETED;
        String value = status.getValue();

        // Assert
        assertThat(value).isEqualTo("completed");
    }

    @Test
    @DisplayName("TaskStatus: all values should be non-null strings")
    void taskStatus_AllValuesShouldBeNonNullStrings() {
        // Arrange & Act
        for (TaskStatus status : TaskStatus.values()) {
            String value = status.getValue();

            // Assert
            assertThat(value).isNotNull();
            assertThat(value).isNotEmpty();
            assertThat(value).isInstanceOf(String.class);
        }
    }

    @Test
    @DisplayName("TaskStatus: all values should contain only lowercase letters and underscores")
    void taskStatus_ValuesShouldContainOnlyLowercaseAndUnderscores() {
        // Arrange & Act
        for (TaskStatus status : TaskStatus.values()) {
            String value = status.getValue();

            // Assert
            assertThat(value).matches("^[a-z_]+$");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ────────────── FROM VALUE FACTORY METHOD TESTS ──────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("TaskStatus.fromValue: should convert 'pending' to PENDING")
    void taskStatus_FromValue_ShouldConvertPending() {
        // Arrange & Act
        TaskStatus status = TaskStatus.fromValue("pending");

        // Assert
        assertThat(status).isEqualTo(TaskStatus.PENDING);
    }

    @Test
    @DisplayName("TaskStatus.fromValue: should convert 'in_progress' to IN_PROGRESS")
    void taskStatus_FromValue_ShouldConvertInProgress() {
        // Arrange & Act
        TaskStatus status = TaskStatus.fromValue("in_progress");

        // Assert
        assertThat(status).isEqualTo(TaskStatus.IN_PROGRESS);
    }

    @Test
    @DisplayName("TaskStatus.fromValue: should convert 'completed' to COMPLETED")
    void taskStatus_FromValue_ShouldConvertCompleted() {
        // Arrange & Act
        TaskStatus status = TaskStatus.fromValue("completed");

        // Assert
        assertThat(status).isEqualTo(TaskStatus.COMPLETED);
    }

    @Test
    @DisplayName("TaskStatus.fromValue: should be case-insensitive (uppercase)")
    void taskStatus_FromValue_ShouldBeCaseInsensitiveUppercase() {
        // Arrange & Act
        TaskStatus pendingUpper = TaskStatus.fromValue("PENDING");
        TaskStatus inProgressUpper = TaskStatus.fromValue("IN_PROGRESS");
        TaskStatus completedUpper = TaskStatus.fromValue("COMPLETED");

        // Assert
        assertThat(pendingUpper).isEqualTo(TaskStatus.PENDING);
        assertThat(inProgressUpper).isEqualTo(TaskStatus.IN_PROGRESS);
        assertThat(completedUpper).isEqualTo(TaskStatus.COMPLETED);
    }

    @Test
    @DisplayName("TaskStatus.fromValue: should be case-insensitive (mixed case)")
    void taskStatus_FromValue_ShouldBeCaseInsensitiveMixedCase() {
        // Arrange & Act
        TaskStatus pending = TaskStatus.fromValue("PeNdInG");
        TaskStatus inProgress = TaskStatus.fromValue("In_Progress");
        TaskStatus completed = TaskStatus.fromValue("CoMpLeTeD");

        // Assert
        assertThat(pending).isEqualTo(TaskStatus.PENDING);
        assertThat(inProgress).isEqualTo(TaskStatus.IN_PROGRESS);
        assertThat(completed).isEqualTo(TaskStatus.COMPLETED);
    }

    @ParameterizedTest
    @ValueSource(strings = {"pending", "PENDING", "Pending", "PeNdInG"})
    @DisplayName("TaskStatus.fromValue: should accept various case variations of pending")
    void taskStatus_FromValue_PendingVariations(String value) {
        // Act
        TaskStatus status = TaskStatus.fromValue(value);

        // Assert
        assertThat(status).isEqualTo(TaskStatus.PENDING);
    }

    @ParameterizedTest
    @ValueSource(strings = {"in_progress", "IN_PROGRESS", "In_Progress", "IN_progress"})
    @DisplayName("TaskStatus.fromValue: should accept various case variations of in_progress")
    void taskStatus_FromValue_InProgressVariations(String value) {
        // Act
        TaskStatus status = TaskStatus.fromValue(value);

        // Assert
        assertThat(status).isEqualTo(TaskStatus.IN_PROGRESS);
    }

    @ParameterizedTest
    @ValueSource(strings = {"completed", "COMPLETED", "Completed", "CoMpLeTeD"})
    @DisplayName("TaskStatus.fromValue: should accept various case variations of completed")
    void taskStatus_FromValue_CompletedVariations(String value) {
        // Act
        TaskStatus status = TaskStatus.fromValue(value);

        // Assert
        assertThat(status).isEqualTo(TaskStatus.COMPLETED);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ────────────── INVALID VALUE TESTS ────────────────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("TaskStatus.fromValue: should throw IllegalArgumentException for invalid value")
    void taskStatus_FromValue_InvalidValue_ShouldThrow() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> TaskStatus.fromValue("invalid"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("TaskStatus.fromValue: should throw IllegalArgumentException for empty string")
    void taskStatus_FromValue_EmptyString_ShouldThrow() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> TaskStatus.fromValue(""))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("TaskStatus.fromValue: should throw IllegalArgumentException for null value")
    void taskStatus_FromValue_NullValue_ShouldThrow() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> TaskStatus.fromValue(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("TaskStatus.fromValue: should throw IllegalArgumentException with descriptive message")
    void taskStatus_FromValue_ShouldThrowWithDescriptiveMessage() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> TaskStatus.fromValue("unknown"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid status: 'unknown'. Must be one of: pending, in_progress, completed");
    }

    @Test
    @DisplayName("TaskStatus.fromValue: error message should include invalid value")
    void taskStatus_FromValue_ErrorMessageShouldIncludeInvalidValue() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> TaskStatus.fromValue("bad-status"))
                .hasMessageContaining("bad-status");
    }

    @Test
    @DisplayName("TaskStatus.fromValue: error message should list valid options")
    void taskStatus_FromValue_ErrorMessageShouldListValidOptions() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> TaskStatus.fromValue("random"))
                .hasMessageContaining("pending")
                .hasMessageContaining("in_progress")
                .hasMessageContaining("completed");
    }

    @ParameterizedTest
    @ValueSource(strings = {"pend", "in-progress", "complete", "READY", "done", "pending-task"})
    @DisplayName("TaskStatus.fromValue: should throw for various invalid values")
    void taskStatus_FromValue_VariousInvalidValues_ShouldThrow(String invalidValue) {
        // Act & Assert
        assertThatThrownBy(() -> TaskStatus.fromValue(invalidValue))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ────────────── ROUND-TRIP CONVERSION TESTS ──────────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("TaskStatus: should support round-trip conversion (enum -> value -> enum)")
    void taskStatus_RoundTripConversion() {
        // Arrange & Act
        for (TaskStatus originalStatus : TaskStatus.values()) {
            String value = originalStatus.getValue();
            TaskStatus recoveredStatus = TaskStatus.fromValue(value);

            // Assert
            assertThat(recoveredStatus).isEqualTo(originalStatus);
        }
    }

    @Test
    @DisplayName("TaskStatus: PENDING round-trip should preserve value")
    void taskStatus_PendingRoundTrip() {
        // Arrange & Act
        TaskStatus original = TaskStatus.PENDING;
        String value = original.getValue();
        TaskStatus recovered = TaskStatus.fromValue(value);

        // Assert
        assertThat(recovered).isEqualTo(original);
        assertThat(recovered.getValue()).isEqualTo(value);
    }

    @Test
    @DisplayName("TaskStatus: IN_PROGRESS round-trip should preserve value")
    void taskStatus_InProgressRoundTrip() {
        // Arrange & Act
        TaskStatus original = TaskStatus.IN_PROGRESS;
        String value = original.getValue();
        TaskStatus recovered = TaskStatus.fromValue(value);

        // Assert
        assertThat(recovered).isEqualTo(original);
        assertThat(recovered.getValue()).isEqualTo(value);
    }

    @Test
    @DisplayName("TaskStatus: COMPLETED round-trip should preserve value")
    void taskStatus_CompletedRoundTrip() {
        // Arrange & Act
        TaskStatus original = TaskStatus.COMPLETED;
        String value = original.getValue();
        TaskStatus recovered = TaskStatus.fromValue(value);

        // Assert
        assertThat(recovered).isEqualTo(original);
        assertThat(recovered.getValue()).isEqualTo(value);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ────────────── JSON ANNOTATION TESTS ────────────────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("TaskStatus: should have @JsonValue annotation on getValue")
    void taskStatus_ShouldHaveJsonValueAnnotation() {
        // Arrange & Act
        java.lang.reflect.Method getValueMethod = null;
        try {
            getValueMethod = TaskStatus.class.getMethod("getValue");
        } catch (NoSuchMethodException e) {
            fail("getValue method not found");
        }

        // Assert
        assertThat(getValueMethod).isNotNull();
        assertThat(getValueMethod.isAnnotationPresent(com.fasterxml.jackson.annotation.JsonValue.class)).isTrue();
    }

    @Test
    @DisplayName("TaskStatus: should have @JsonCreator annotation on fromValue")
    void taskStatus_ShouldHaveJsonCreatorAnnotation() {
        // Arrange & Act
        java.lang.reflect.Method fromValueMethod = null;
        try {
            fromValueMethod = TaskStatus.class.getMethod("fromValue", String.class);
        } catch (NoSuchMethodException e) {
            fail("fromValue method not found");
        }

        // Assert
        assertThat(fromValueMethod).isNotNull();
        assertThat(fromValueMethod.isAnnotationPresent(com.fasterxml.jackson.annotation.JsonCreator.class)).isTrue();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ────────────── ENUM COMPARISON TESTS ────────────────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("TaskStatus: should support equality comparison")
    void taskStatus_ShouldSupportEqualityComparison() {
        // Arrange & Act
        TaskStatus pending1 = TaskStatus.PENDING;
        TaskStatus pending2 = TaskStatus.PENDING;
        TaskStatus inProgress = TaskStatus.IN_PROGRESS;

        // Assert
        assertThat(pending1).isEqualTo(pending2);
        assertThat(pending1).isNotEqualTo(inProgress);
    }

    @Test
    @DisplayName("TaskStatus: enum instances should be the same object (singleton)")
    void taskStatus_ShouldBeSingleton() {
        // Arrange & Act
        TaskStatus pending1 = TaskStatus.PENDING;
        TaskStatus pending2 = TaskStatus.PENDING;

        // Assert
        assertThat(pending1).isSameAs(pending2);
    }

    @Test
    @DisplayName("TaskStatus: should support ordinal comparison")
    void taskStatus_ShouldSupportOrdinalComparison() {
        // Arrange & Act
        int pendingOrdinal = TaskStatus.PENDING.ordinal();
        int inProgressOrdinal = TaskStatus.IN_PROGRESS.ordinal();
        int completedOrdinal = TaskStatus.COMPLETED.ordinal();

        // Assert
        assertThat(pendingOrdinal).isLessThan(inProgressOrdinal);
        assertThat(inProgressOrdinal).isLessThan(completedOrdinal);
    }

    @Test
    @DisplayName("TaskStatus: should support name method")
    void taskStatus_ShouldSupportNameMethod() {
        // Arrange & Act
        String pendingName = TaskStatus.PENDING.name();
        String inProgressName = TaskStatus.IN_PROGRESS.name();
        String completedName = TaskStatus.COMPLETED.name();

        // Assert
        assertThat(pendingName).isEqualTo("PENDING");
        assertThat(inProgressName).isEqualTo("IN_PROGRESS");
        assertThat(completedName).isEqualTo("COMPLETED");
    }

    @Test
    @DisplayName("TaskStatus: enum name should be different from value")
    void taskStatus_EnumNameShouldBeDifferentFromValue() {
        // Arrange & Act
        TaskStatus status = TaskStatus.IN_PROGRESS;
        String name = status.name();
        String value = status.getValue();

        // Assert
        assertThat(name).isNotEqualTo(value);
        assertThat(name).isEqualTo("IN_PROGRESS");
        assertThat(value).isEqualTo("in_progress");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ────────────── SWITCH/MATCH TESTS ──────────────────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("TaskStatus: should be usable in switch statement")
    void taskStatus_ShouldBeUsableInSwitch() {
        // Arrange
        String result = "";

        // Act
        TaskStatus status = TaskStatus.IN_PROGRESS;
        switch (status) {
            case PENDING:
                result = "not-started";
                break;
            case IN_PROGRESS:
                result = "in-work";
                break;
            case COMPLETED:
                result = "finished";
                break;
        }

        // Assert
        assertThat(result).isEqualTo("in-work");
    }

    @Test
    @DisplayName("TaskStatus: should handle all cases in switch statement")
    void taskStatus_SwitchShouldHandleAllCases() {
        // Arrange
        for (TaskStatus status : TaskStatus.values()) {
            String result = "";

            // Act
            switch (status) {
                case PENDING:
                    result = "pending";
                    break;
                case IN_PROGRESS:
                    result = "in_progress";
                    break;
                case COMPLETED:
                    result = "completed";
                    break;
            }

            // Assert
            assertThat(result).isNotEmpty();
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ────────────── PRACTICAL USE CASE TESTS ────────────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("TaskStatus: should convert from JSON string to enum")
    void taskStatus_JsonDeserialization() {
        // Arrange
        String jsonValue = "in_progress";

        // Act
        TaskStatus status = TaskStatus.fromValue(jsonValue);

        // Assert
        assertThat(status).isEqualTo(TaskStatus.IN_PROGRESS);
        assertThat(status.getValue()).isEqualTo(jsonValue);
    }

    @Test
    @DisplayName("TaskStatus: should convert from enum to JSON string")
    void taskStatus_JsonSerialization() {
        // Arrange
        TaskStatus status = TaskStatus.COMPLETED;

        // Act
        String jsonValue = status.getValue();

        // Assert
        assertThat(jsonValue).isEqualTo("completed");
    }

    @Test
    @DisplayName("TaskStatus: workflow progression should be valid")
    void taskStatus_WorkflowProgressionValid() {
        // Arrange
        TaskStatus currentStatus = TaskStatus.PENDING;

        // Act & Assert - Progress to IN_PROGRESS
        assertThat(currentStatus).isEqualTo(TaskStatus.PENDING);
        currentStatus = TaskStatus.IN_PROGRESS;
        assertThat(currentStatus).isEqualTo(TaskStatus.IN_PROGRESS);

        // Act & Assert - Progress to COMPLETED
        currentStatus = TaskStatus.COMPLETED;
        assertThat(currentStatus).isEqualTo(TaskStatus.COMPLETED);
    }

    @Test
    @DisplayName("TaskStatus: should support iteration over all statuses")
    void taskStatus_ShouldSupportIteration() {
        // Arrange & Act
        int count = 0;
        for (TaskStatus status : TaskStatus.values()) {
            assertThat(status).isNotNull();
            count++;
        }

        // Assert
        assertThat(count).isEqualTo(3);
    }

    @Test
    @DisplayName("TaskStatus: should be serializable")
    void taskStatus_ShouldBeSerializable() {
        // Arrange & Act
        TaskStatus status = TaskStatus.PENDING;

        // Assert
        assertThat(status).isInstanceOf(Enum.class);
        assertThat(status).isInstanceOf(java.io.Serializable.class);
    }

    @Test
    @DisplayName("TaskStatus: should have consistent hashCode")
    void taskStatus_ShouldHaveConsistentHashCode() {
        // Arrange & Act
        TaskStatus pending1 = TaskStatus.PENDING;
        TaskStatus pending2 = TaskStatus.PENDING;

        // Assert
        assertThat(pending1.hashCode()).isEqualTo(pending2.hashCode());
    }

    @Test
    @DisplayName("TaskStatus: should have valid toString representation")
    void taskStatus_ShouldHaveValidToString() {
        // Arrange & Act
        TaskStatus status = TaskStatus.IN_PROGRESS;
        String toStringResult = status.toString();

        // Assert
        assertThat(toStringResult).isNotNull();
        assertThat(toStringResult).contains("IN_PROGRESS");
    }

    @ParameterizedTest
    @CsvSource({
            "pending, PENDING",
            "in_progress, IN_PROGRESS",
            "completed, COMPLETED"
    })
    @DisplayName("TaskStatus: parameterized round-trip conversion test")
    void taskStatus_ParameterizedRoundTripConversion(String value, String expectedName) {
        // Act
        TaskStatus status = TaskStatus.fromValue(value);

        // Assert
        assertThat(status.getValue()).isEqualTo(value);
        assertThat(status.name()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("TaskStatus: multiple conversions should maintain consistency")
    void taskStatus_MultipleConversionsShouldBConsistent() {
        // Arrange
        String value = "completed";

        // Act
        TaskStatus status1 = TaskStatus.fromValue(value);
        TaskStatus status2 = TaskStatus.fromValue(value);
        TaskStatus status3 = TaskStatus.fromValue(status1.getValue());

        // Assert
        assertThat(status1).isEqualTo(status2).isEqualTo(status3);
        assertThat(status1.getValue()).isEqualTo(status2.getValue()).isEqualTo(status3.getValue());
    }
}

