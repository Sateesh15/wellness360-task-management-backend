package com.wellness360.taskmanagement.dto;

import com.wellness360.taskmanagement.model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for TaskDto classes — covers CreateRequest, UpdateRequest, Response, and ApiResponse.
 * Tests validation constraints, data integrity, and builder patterns.
 */
@DisplayName("TaskDto Tests")
class TaskDtoTest {

    // ─────────────────────────────────────────────────────────────────────────
    // ────────────────── CREATE REQUEST DTO TESTS ──────────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("CreateRequest: should create instance with valid data")
    void createRequest_WithValidData_ShouldCreateInstance() {
        // Arrange & Act
        TaskDto.CreateRequest createRequest = new TaskDto.CreateRequest(
                "Complete Project",
                "Finish the Spring Boot project",
                LocalDate.now().plusDays(7)
        );

        // Assert
        assertThat(createRequest).isNotNull();
        assertThat(createRequest.getTitle()).isEqualTo("Complete Project");
        assertThat(createRequest.getDescription()).isEqualTo("Finish the Spring Boot project");
        assertThat(createRequest.getDueDate()).isEqualTo(LocalDate.now().plusDays(7));
    }

    @Test
    @DisplayName("CreateRequest: should have default constructor (no-arg)")
    void createRequest_ShouldHaveNoArgConstructor() {
        // Arrange & Act
        TaskDto.CreateRequest createRequest = new TaskDto.CreateRequest();

        // Assert
        assertThat(createRequest).isNotNull();
        assertThat(createRequest.getTitle()).isNull();
        assertThat(createRequest.getDescription()).isNull();
        assertThat(createRequest.getDueDate()).isNull();
    }

    @Test
    @DisplayName("CreateRequest: should set title via setter")
    void createRequest_SetTitle_ShouldUpdateField() {
        // Arrange
        TaskDto.CreateRequest createRequest = new TaskDto.CreateRequest();

        // Act
        createRequest.setTitle("New Task");

        // Assert
        assertThat(createRequest.getTitle()).isEqualTo("New Task");
    }

    @Test
    @DisplayName("CreateRequest: should set description via setter")
    void createRequest_SetDescription_ShouldUpdateField() {
        // Arrange
        TaskDto.CreateRequest createRequest = new TaskDto.CreateRequest();

        // Act
        createRequest.setDescription("Task description");

        // Assert
        assertThat(createRequest.getDescription()).isEqualTo("Task description");
    }

    @Test
    @DisplayName("CreateRequest: should set due date via setter")
    void createRequest_SetDueDate_ShouldUpdateField() {
        // Arrange
        TaskDto.CreateRequest createRequest = new TaskDto.CreateRequest();
        LocalDate futureDate = LocalDate.now().plusDays(5);

        // Act
        createRequest.setDueDate(futureDate);

        // Assert
        assertThat(createRequest.getDueDate()).isEqualTo(futureDate);
    }

    @Test
    @DisplayName("CreateRequest: should accept title with minimum length (1 character)")
    void createRequest_WithMinimumTitleLength_ShouldSucceed() {
        // Arrange & Act
        TaskDto.CreateRequest createRequest = new TaskDto.CreateRequest(
                "A",
                "Description",
                LocalDate.now().plusDays(1)
        );

        // Assert
        assertThat(createRequest.getTitle()).isEqualTo("A");
    }

    @Test
    @DisplayName("CreateRequest: should accept title with maximum length (200 characters)")
    void createRequest_WithMaximumTitleLength_ShouldSucceed() {
        // Arrange
        String maxTitle = "A".repeat(200);

        // Act
        TaskDto.CreateRequest createRequest = new TaskDto.CreateRequest(
                maxTitle,
                "Description",
                LocalDate.now().plusDays(1)
        );

        // Assert
        assertThat(createRequest.getTitle()).hasSize(200);
    }

    @Test
    @DisplayName("CreateRequest: should allow null description")
    void createRequest_WithNullDescription_ShouldSucceed() {
        // Arrange & Act
        TaskDto.CreateRequest createRequest = new TaskDto.CreateRequest(
                "Task Title",
                null,
                LocalDate.now().plusDays(5)
        );

        // Assert
        assertThat(createRequest.getDescription()).isNull();
    }

    @Test
    @DisplayName("CreateRequest: should accept description with maximum length (1000 characters)")
    void createRequest_WithMaximumDescriptionLength_ShouldSucceed() {
        // Arrange
        String maxDescription = "A".repeat(1000);

        // Act
        TaskDto.CreateRequest createRequest = new TaskDto.CreateRequest(
                "Task Title",
                maxDescription,
                LocalDate.now().plusDays(1)
        );

        // Assert
        assertThat(createRequest.getDescription()).hasSize(1000);
    }

    @Test
    @DisplayName("CreateRequest: should accept due date as today")
    void createRequest_WithTodayDueDate_ShouldSucceed() {
        // Arrange & Act
        TaskDto.CreateRequest createRequest = new TaskDto.CreateRequest(
                "Task Title",
                "Description",
                LocalDate.now()
        );

        // Assert
        assertThat(createRequest.getDueDate()).isEqualTo(LocalDate.now());
    }

    @Test
    @DisplayName("CreateRequest: should accept future due date")
    void createRequest_WithFutureDueDate_ShouldSucceed() {
        // Arrange
        LocalDate futureDate = LocalDate.now().plusYears(1);

        // Act
        TaskDto.CreateRequest createRequest = new TaskDto.CreateRequest(
                "Task Title",
                "Description",
                futureDate
        );

        // Assert
        assertThat(createRequest.getDueDate()).isEqualTo(futureDate);
    }

    @Test
    @DisplayName("CreateRequest: should handle empty string title")
    void createRequest_WithEmptyTitle_ShouldStore() {
        // Arrange & Act
        TaskDto.CreateRequest createRequest = new TaskDto.CreateRequest(
                "",
                "Description",
                LocalDate.now().plusDays(1)
        );

        // Assert
        assertThat(createRequest.getTitle()).isEmpty();
    }

    @Test
    @DisplayName("CreateRequest: should handle empty string description")
    void createRequest_WithEmptyDescription_ShouldStore() {
        // Arrange & Act
        TaskDto.CreateRequest createRequest = new TaskDto.CreateRequest(
                "Task Title",
                "",
                LocalDate.now().plusDays(1)
        );

        // Assert
        assertThat(createRequest.getDescription()).isEmpty();
    }

    @Test
    @DisplayName("CreateRequest: should handle special characters in title")
    void createRequest_WithSpecialCharactersInTitle_ShouldStore() {
        // Arrange & Act
        TaskDto.CreateRequest createRequest = new TaskDto.CreateRequest(
                "Task: Complete & Review @50% #Urgent!",
                "Description",
                LocalDate.now().plusDays(1)
        );

        // Assert
        assertThat(createRequest.getTitle()).isEqualTo("Task: Complete & Review @50% #Urgent!");
    }

    @Test
    @DisplayName("CreateRequest: should handle special characters in description")
    void createRequest_WithSpecialCharactersInDescription_ShouldStore() {
        // Arrange & Act
        TaskDto.CreateRequest createRequest = new TaskDto.CreateRequest(
                "Task Title",
                "Description with special chars: @#$%^&*()",
                LocalDate.now().plusDays(1)
        );

        // Assert
        assertThat(createRequest.getDescription()).contains("@#$%^&*()");
    }

    @Test
    @DisplayName("CreateRequest: should allow null due date")
    void createRequest_WithNullDueDate_ShouldStore() {
        // Arrange & Act
        TaskDto.CreateRequest createRequest = new TaskDto.CreateRequest(
                "Task Title",
                "Description",
                null
        );

        // Assert
        assertThat(createRequest.getDueDate()).isNull();
    }

    @Test
    @DisplayName("CreateRequest: should handle whitespace in title")
    void createRequest_WithWhitespaceInTitle_ShouldStore() {
        // Arrange & Act
        TaskDto.CreateRequest createRequest = new TaskDto.CreateRequest(
                "Task with spaces and\ttabs",
                "Description",
                LocalDate.now().plusDays(1)
        );

        // Assert
        assertThat(createRequest.getTitle()).contains("spaces");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ────────────────── UPDATE REQUEST DTO TESTS ──────────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("UpdateRequest: should create instance with valid data")
    void updateRequest_WithValidData_ShouldCreateInstance() {
        // Arrange & Act
        TaskDto.UpdateRequest updateRequest = new TaskDto.UpdateRequest(
                "Updated Title",
                "Updated Description",
                LocalDate.now().plusDays(10),
                TaskStatus.COMPLETED
        );

        // Assert
        assertThat(updateRequest).isNotNull();
        assertThat(updateRequest.getTitle()).isEqualTo("Updated Title");
        assertThat(updateRequest.getDescription()).isEqualTo("Updated Description");
        assertThat(updateRequest.getDueDate()).isEqualTo(LocalDate.now().plusDays(10));
        assertThat(updateRequest.getStatus()).isEqualTo(TaskStatus.COMPLETED);
    }

    @Test
    @DisplayName("UpdateRequest: should have default constructor (no-arg)")
    void updateRequest_ShouldHaveNoArgConstructor() {
        // Arrange & Act
        TaskDto.UpdateRequest updateRequest = new TaskDto.UpdateRequest();

        // Assert
        assertThat(updateRequest).isNotNull();
        assertThat(updateRequest.getTitle()).isNull();
        assertThat(updateRequest.getDescription()).isNull();
        assertThat(updateRequest.getDueDate()).isNull();
        assertThat(updateRequest.getStatus()).isNull();
    }

    @Test
    @DisplayName("UpdateRequest: should set title via setter")
    void updateRequest_SetTitle_ShouldUpdateField() {
        // Arrange
        TaskDto.UpdateRequest updateRequest = new TaskDto.UpdateRequest();

        // Act
        updateRequest.setTitle("Updated Title");

        // Assert
        assertThat(updateRequest.getTitle()).isEqualTo("Updated Title");
    }

    @Test
    @DisplayName("UpdateRequest: should set description via setter")
    void updateRequest_SetDescription_ShouldUpdateField() {
        // Arrange
        TaskDto.UpdateRequest updateRequest = new TaskDto.UpdateRequest();

        // Act
        updateRequest.setDescription("Updated Description");

        // Assert
        assertThat(updateRequest.getDescription()).isEqualTo("Updated Description");
    }

    @Test
    @DisplayName("UpdateRequest: should set due date via setter")
    void updateRequest_SetDueDate_ShouldUpdateField() {
        // Arrange
        TaskDto.UpdateRequest updateRequest = new TaskDto.UpdateRequest();
        LocalDate newDate = LocalDate.now().plusDays(5);

        // Act
        updateRequest.setDueDate(newDate);

        // Assert
        assertThat(updateRequest.getDueDate()).isEqualTo(newDate);
    }

    @Test
    @DisplayName("UpdateRequest: should set status via setter")
    void updateRequest_SetStatus_ShouldUpdateField() {
        // Arrange
        TaskDto.UpdateRequest updateRequest = new TaskDto.UpdateRequest();

        // Act
        updateRequest.setStatus(TaskStatus.IN_PROGRESS);

        // Assert
        assertThat(updateRequest.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
    }

    @Test
    @DisplayName("UpdateRequest: should accept all task statuses")
    void updateRequest_WithAllTaskStatuses_ShouldStore() {
        // Arrange & Act & Assert
        for (TaskStatus status : TaskStatus.values()) {
            TaskDto.UpdateRequest updateRequest = new TaskDto.UpdateRequest(
                    "Title",
                    "Description",
                    LocalDate.now().plusDays(1),
                    status
            );
            assertThat(updateRequest.getStatus()).isEqualTo(status);
        }
    }

    @Test
    @DisplayName("UpdateRequest: should allow partial updates (null fields)")
    void updateRequest_WithNullFields_ShouldStore() {
        // Arrange & Act
        TaskDto.UpdateRequest updateRequest = new TaskDto.UpdateRequest(
                null,
                null,
                null,
                null
        );

        // Assert
        assertThat(updateRequest.getTitle()).isNull();
        assertThat(updateRequest.getDescription()).isNull();
        assertThat(updateRequest.getDueDate()).isNull();
        assertThat(updateRequest.getStatus()).isNull();
    }

    @Test
    @DisplayName("UpdateRequest: should allow updating only title")
    void updateRequest_WithOnlyTitle_ShouldStore() {
        // Arrange & Act
        TaskDto.UpdateRequest updateRequest = new TaskDto.UpdateRequest(
                "Only Title",
                null,
                null,
                null
        );

        // Assert
        assertThat(updateRequest.getTitle()).isEqualTo("Only Title");
        assertThat(updateRequest.getDescription()).isNull();
        assertThat(updateRequest.getDueDate()).isNull();
        assertThat(updateRequest.getStatus()).isNull();
    }

    @Test
    @DisplayName("UpdateRequest: should allow updating only status")
    void updateRequest_WithOnlyStatus_ShouldStore() {
        // Arrange & Act
        TaskDto.UpdateRequest updateRequest = new TaskDto.UpdateRequest(
                null,
                null,
                null,
                TaskStatus.PENDING
        );

        // Assert
        assertThat(updateRequest.getTitle()).isNull();
        assertThat(updateRequest.getStatus()).isEqualTo(TaskStatus.PENDING);
    }

    @Test
    @DisplayName("UpdateRequest: should accept title with maximum length")
    void updateRequest_WithMaximumTitleLength_ShouldSucceed() {
        // Arrange
        String maxTitle = "B".repeat(200);

        // Act
        TaskDto.UpdateRequest updateRequest = new TaskDto.UpdateRequest(
                maxTitle,
                null,
                null,
                null
        );

        // Assert
        assertThat(updateRequest.getTitle()).hasSize(200);
    }

    @Test
    @DisplayName("UpdateRequest: should accept description with maximum length")
    void updateRequest_WithMaximumDescriptionLength_ShouldSucceed() {
        // Arrange
        String maxDescription = "B".repeat(1000);

        // Act
        TaskDto.UpdateRequest updateRequest = new TaskDto.UpdateRequest(
                "Title",
                maxDescription,
                null,
                null
        );

        // Assert
        assertThat(updateRequest.getDescription()).hasSize(1000);
    }

    @Test
    @DisplayName("UpdateRequest: should handle empty string fields")
    void updateRequest_WithEmptyStrings_ShouldStore() {
        // Arrange & Act
        TaskDto.UpdateRequest updateRequest = new TaskDto.UpdateRequest("", "", null, null);

        // Assert
        assertThat(updateRequest.getTitle()).isEmpty();
        assertThat(updateRequest.getDescription()).isEmpty();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ────────────────── RESPONSE DTO TESTS ──────────────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Response: should create instance with builder pattern")
    void response_WithBuilder_ShouldCreateInstance() {
        // Arrange & Act
        TaskDto.Response response = TaskDto.Response.builder()
                .id("task-1")
                .title("Complete Project")
                .description("Finish the Spring Boot project")
                .dueDate(LocalDate.now().plusDays(7))
                .status("pending")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo("task-1");
        assertThat(response.getTitle()).isEqualTo("Complete Project");
        assertThat(response.getDescription()).isEqualTo("Finish the Spring Boot project");
        assertThat(response.getStatus()).isEqualTo("pending");
    }

    @Test
    @DisplayName("Response: should have default constructor (no-arg)")
    void response_ShouldHaveNoArgConstructor() {
        // Arrange & Act
        TaskDto.Response response = new TaskDto.Response();

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getId()).isNull();
        assertThat(response.getTitle()).isNull();
        assertThat(response.getStatus()).isNull();
    }

    @Test
    @DisplayName("Response: should create instance with all-arg constructor")
    void response_WithAllArgConstructor_ShouldCreateInstance() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();

        // Act
        TaskDto.Response response = new TaskDto.Response(
                "task-1",
                "Title",
                "Description",
                LocalDate.now().plusDays(5),
                "pending",
                now,
                now
        );

        // Assert
        assertThat(response.getId()).isEqualTo("task-1");
        assertThat(response.getTitle()).isEqualTo("Title");
        assertThat(response.getStatus()).isEqualTo("pending");
    }

    @Test
    @DisplayName("Response: should set id via setter")
    void response_SetId_ShouldUpdateField() {
        // Arrange
        TaskDto.Response response = new TaskDto.Response();

        // Act
        response.setId("new-id");

        // Assert
        assertThat(response.getId()).isEqualTo("new-id");
    }

    @Test
    @DisplayName("Response: should set title via setter")
    void response_SetTitle_ShouldUpdateField() {
        // Arrange
        TaskDto.Response response = new TaskDto.Response();

        // Act
        response.setTitle("New Title");

        // Assert
        assertThat(response.getTitle()).isEqualTo("New Title");
    }

    @Test
    @DisplayName("Response: should set description via setter")
    void response_SetDescription_ShouldUpdateField() {
        // Arrange
        TaskDto.Response response = new TaskDto.Response();

        // Act
        response.setDescription("New Description");

        // Assert
        assertThat(response.getDescription()).isEqualTo("New Description");
    }

    @Test
    @DisplayName("Response: should set due date via setter")
    void response_SetDueDate_ShouldUpdateField() {
        // Arrange
        TaskDto.Response response = new TaskDto.Response();
        LocalDate dueDate = LocalDate.now().plusDays(5);

        // Act
        response.setDueDate(dueDate);

        // Assert
        assertThat(response.getDueDate()).isEqualTo(dueDate);
    }

    @Test
    @DisplayName("Response: should set status via setter")
    void response_SetStatus_ShouldUpdateField() {
        // Arrange
        TaskDto.Response response = new TaskDto.Response();

        // Act
        response.setStatus("completed");

        // Assert
        assertThat(response.getStatus()).isEqualTo("completed");
    }

    @Test
    @DisplayName("Response: should set timestamps via setters")
    void response_SetTimestamps_ShouldUpdateFields() {
        // Arrange
        TaskDto.Response response = new TaskDto.Response();
        LocalDateTime now = LocalDateTime.now();

        // Act
        response.setCreatedAt(now);
        response.setUpdatedAt(now);

        // Assert
        assertThat(response.getCreatedAt()).isEqualTo(now);
        assertThat(response.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Response: builder should allow partial configuration")
    void response_BuilderPartial_ShouldCreateWithNullFields() {
        // Arrange & Act
        TaskDto.Response response = TaskDto.Response.builder()
                .id("task-1")
                .title("Title")
                .build();

        // Assert
        assertThat(response.getId()).isEqualTo("task-1");
        assertThat(response.getTitle()).isEqualTo("Title");
        assertThat(response.getDescription()).isNull();
        assertThat(response.getDueDate()).isNull();
        assertThat(response.getStatus()).isNull();
    }

    @Test
    @DisplayName("Response: should handle all task status values")
    void response_WithAllTaskStatuses_ShouldStore() {
        // Arrange, Act & Assert
        String[] statuses = {"pending", "in-progress", "completed"};
        for (String status : statuses) {
            TaskDto.Response response = TaskDto.Response.builder()
                    .id("task-1")
                    .status(status)
                    .build();
            assertThat(response.getStatus()).isEqualTo(status);
        }
    }

    @Test
    @DisplayName("Response: should allow null description")
    void response_WithNullDescription_ShouldStore() {
        // Arrange & Act
        TaskDto.Response response = TaskDto.Response.builder()
                .id("task-1")
                .title("Title")
                .description(null)
                .build();

        // Assert
        assertThat(response.getDescription()).isNull();
    }

    @Test
    @DisplayName("Response: should allow null due date")
    void response_WithNullDueDate_ShouldStore() {
        // Arrange & Act
        TaskDto.Response response = TaskDto.Response.builder()
                .id("task-1")
                .title("Title")
                .dueDate(null)
                .build();

        // Assert
        assertThat(response.getDueDate()).isNull();
    }

    @Test
    @DisplayName("Response: builder should work with multiple builds")
    void response_MultipleBuildCalls_ShouldCreateIndependentInstances() {
        // Arrange & Act
        TaskDto.Response response1 = TaskDto.Response.builder()
                .id("task-1")
                .title("Title1")
                .build();

        TaskDto.Response response2 = TaskDto.Response.builder()
                .id("task-2")
                .title("Title2")
                .build();

        // Assert
        assertThat(response1.getId()).isEqualTo("task-1");
        assertThat(response2.getId()).isEqualTo("task-2");
        assertThat(response1.getTitle()).isEqualTo("Title1");
        assertThat(response2.getTitle()).isEqualTo("Title2");
    }

    @Test
    @DisplayName("Response: should handle UUID-like ids")
    void response_WithUuidId_ShouldStore() {
        // Arrange
        String uuid = "550e8400-e29b-41d4-a716-446655440000";

        // Act
        TaskDto.Response response = TaskDto.Response.builder()
                .id(uuid)
                .build();

        // Assert
        assertThat(response.getId()).isEqualTo(uuid);
    }

    @Test
    @DisplayName("Response: should store timestamps with precision")
    void response_WithPreciseTimestamps_ShouldStore() {
        // Arrange
        LocalDateTime createdAt = LocalDateTime.of(2025, 1, 15, 10, 30, 45);
        LocalDateTime updatedAt = LocalDateTime.of(2025, 1, 16, 14, 20, 30);

        // Act
        TaskDto.Response response = TaskDto.Response.builder()
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        // Assert
        assertThat(response.getCreatedAt()).isEqualTo(createdAt);
        assertThat(response.getUpdatedAt()).isEqualTo(updatedAt);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ────────────────── API RESPONSE DTO TESTS ──────────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("ApiResponse: should create instance with builder pattern")
    void apiResponse_WithBuilder_ShouldCreateInstance() {
        // Arrange & Act
        TaskDto.ApiResponse<String> apiResponse = TaskDto.ApiResponse.<String>builder()
                .success(true)
                .message("Success")
                .data("test data")
                .build();

        // Assert
        assertThat(apiResponse).isNotNull();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("Success");
        assertThat(apiResponse.getData()).isEqualTo("test data");
    }

    @Test
    @DisplayName("ApiResponse: should have default constructor (no-arg)")
    void apiResponse_ShouldHaveNoArgConstructor() {
        // Arrange & Act
        TaskDto.ApiResponse<String> apiResponse = new TaskDto.ApiResponse<>();

        // Assert
        assertThat(apiResponse).isNotNull();
    }

    @Test
    @DisplayName("ApiResponse: should create success response using static method")
    void apiResponse_SuccessMethod_ShouldCreateSuccessResponse() {
        // Arrange
        TaskDto.Response taskResponse = TaskDto.Response.builder()
                .id("task-1")
                .title("Title")
                .build();

        // Act
        TaskDto.ApiResponse<TaskDto.Response> apiResponse = TaskDto.ApiResponse.success("Task created", taskResponse);

        // Assert
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("Task created");
        assertThat(apiResponse.getData()).isEqualTo(taskResponse);
    }

    @Test
    @DisplayName("ApiResponse: should create error response using static method")
    void apiResponse_ErrorMethod_ShouldCreateErrorResponse() {
        // Arrange & Act
        TaskDto.ApiResponse<String> apiResponse = TaskDto.ApiResponse.error("Task not found");

        // Assert
        assertThat(apiResponse.isSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("Task not found");
        assertThat(apiResponse.getData()).isNull();
    }

    @Test
    @DisplayName("ApiResponse: should set success flag via setter")
    void apiResponse_SetSuccess_ShouldUpdateField() {
        // Arrange
        TaskDto.ApiResponse<String> apiResponse = new TaskDto.ApiResponse<>();

        // Act
        apiResponse.setSuccess(true);

        // Assert
        assertThat(apiResponse.isSuccess()).isTrue();
    }

    @Test
    @DisplayName("ApiResponse: should set message via setter")
    void apiResponse_SetMessage_ShouldUpdateField() {
        // Arrange
        TaskDto.ApiResponse<String> apiResponse = new TaskDto.ApiResponse<>();

        // Act
        apiResponse.setMessage("Custom message");

        // Assert
        assertThat(apiResponse.getMessage()).isEqualTo("Custom message");
    }

    @Test
    @DisplayName("ApiResponse: should set data via setter")
    void apiResponse_SetData_ShouldUpdateField() {
        // Arrange
        TaskDto.ApiResponse<String> apiResponse = new TaskDto.ApiResponse<>();

        // Act
        apiResponse.setData("test data");

        // Assert
        assertThat(apiResponse.getData()).isEqualTo("test data");
    }

    @Test
    @DisplayName("ApiResponse: should handle generic types with TaskDto.Response")
    void apiResponse_WithTaskResponseGeneric_ShouldStore() {
        // Arrange
        TaskDto.Response taskResponse = TaskDto.Response.builder()
                .id("task-1")
                .title("Title")
                .build();

        // Act
        TaskDto.ApiResponse<TaskDto.Response> apiResponse = TaskDto.ApiResponse.<TaskDto.Response>builder()
                .success(true)
                .message("Success")
                .data(taskResponse)
                .build();

        // Assert
        assertThat(apiResponse.getData().getId()).isEqualTo("task-1");
        assertThat(apiResponse.getData().getTitle()).isEqualTo("Title");
    }

    @Test
    @DisplayName("ApiResponse: should handle generic types with List")
    void apiResponse_WithListGeneric_ShouldStore() {
        // Arrange
        java.util.List<TaskDto.Response> tasks = java.util.Arrays.asList(
                TaskDto.Response.builder().id("1").build(),
                TaskDto.Response.builder().id("2").build()
        );

        // Act
        TaskDto.ApiResponse<java.util.List<TaskDto.Response>> apiResponse = TaskDto.ApiResponse.<java.util.List<TaskDto.Response>>builder()
                .success(true)
                .message("Success")
                .data(tasks)
                .build();

        // Assert
        assertThat(apiResponse.getData()).hasSize(2);
        assertThat(apiResponse.getData().get(0).getId()).isEqualTo("1");
    }

    @Test
    @DisplayName("ApiResponse: should allow null data")
    void apiResponse_WithNullData_ShouldStore() {
        // Arrange & Act
        TaskDto.ApiResponse<String> apiResponse = TaskDto.ApiResponse.<String>builder()
                .success(false)
                .message("Error")
                .data(null)
                .build();

        // Assert
        assertThat(apiResponse.getData()).isNull();
    }

    @Test
    @DisplayName("ApiResponse: should set success to false in error response")
    void apiResponse_ErrorResponse_ShouldHaveSuccessFalse() {
        // Arrange & Act
        TaskDto.ApiResponse<String> apiResponse = TaskDto.ApiResponse.error("Not found");

        // Assert
        assertThat(apiResponse.isSuccess()).isFalse();
    }

    @Test
    @DisplayName("ApiResponse: success method should always set success to true")
    void apiResponse_SuccessResponse_ShouldHaveSuccessTrue() {
        // Arrange & Act
        TaskDto.ApiResponse<String> apiResponse = TaskDto.ApiResponse.success("Operation successful", "data");

        // Assert
        assertThat(apiResponse.isSuccess()).isTrue();
    }

    @Test
    @DisplayName("ApiResponse: builder should allow partial configuration")
    void apiResponse_BuilderPartial_ShouldCreateWithNullFields() {
        // Arrange & Act
        TaskDto.ApiResponse<String> apiResponse = TaskDto.ApiResponse.<String>builder()
                .success(true)
                .build();

        // Assert
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isNull();
        assertThat(apiResponse.getData()).isNull();
    }

    @Test
    @DisplayName("ApiResponse: should build with all fields as null")
    void apiResponse_BuilderAllNull_ShouldCreateInstance() {
        // Arrange & Act
        TaskDto.ApiResponse<String> apiResponse = TaskDto.ApiResponse.<String>builder()
                .success(false)
                .message(null)
                .data(null)
                .build();

        // Assert
        assertThat(apiResponse).isNotNull();
        assertThat(apiResponse.isSuccess()).isFalse();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ────────────────── CROSS-DTO INTERACTION TESTS ──────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("CreateRequest and UpdateRequest should be independent")
    void createAndUpdateRequest_ShouldBeIndependent() {
        // Arrange & Act
        TaskDto.CreateRequest createRequest = new TaskDto.CreateRequest("Create Title", "Create Desc", LocalDate.now());
        TaskDto.UpdateRequest updateRequest = new TaskDto.UpdateRequest("Update Title", "Update Desc", LocalDate.now(), TaskStatus.PENDING);

        // Assert
        assertThat(createRequest.getTitle()).isNotEqualTo(updateRequest.getTitle());
        assertThat(createRequest.getDescription()).isNotEqualTo(updateRequest.getDescription());
    }

    @Test
    @DisplayName("Response should be populatable from CreateRequest")
    void response_CanBePopulatedFromCreateRequest() {
        // Arrange
        TaskDto.CreateRequest createRequest = new TaskDto.CreateRequest(
                "Task Title",
                "Task Description",
                LocalDate.now().plusDays(5)
        );

        // Act
        TaskDto.Response response = TaskDto.Response.builder()
                .id("task-1")
                .title(createRequest.getTitle())
                .description(createRequest.getDescription())
                .dueDate(createRequest.getDueDate())
                .status("pending")
                .build();

        // Assert
        assertThat(response.getTitle()).isEqualTo(createRequest.getTitle());
        assertThat(response.getDescription()).isEqualTo(createRequest.getDescription());
        assertThat(response.getDueDate()).isEqualTo(createRequest.getDueDate());
    }

    @Test
    @DisplayName("ApiResponse should wrap Response successfully")
    void apiResponse_ShouldWrapResponseSuccessfully() {
        // Arrange
        TaskDto.Response response = TaskDto.Response.builder()
                .id("task-1")
                .title("Title")
                .status("pending")
                .build();

        // Act
        TaskDto.ApiResponse<TaskDto.Response> apiResponse = TaskDto.ApiResponse.success("Task retrieved", response);

        // Assert
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getData().getId()).isEqualTo("task-1");
    }

    @Test
    @DisplayName("All TaskDto classes should be instantiable independently")
    void allTaskDtos_ShouldBeInstantiableIndependently() {
        // Arrange & Act
        TaskDto.CreateRequest createRequest = new TaskDto.CreateRequest("Title", "Desc", LocalDate.now());
        TaskDto.UpdateRequest updateRequest = new TaskDto.UpdateRequest("Title", "Desc", LocalDate.now(), TaskStatus.PENDING);
        TaskDto.Response response = TaskDto.Response.builder().id("1").build();
        TaskDto.ApiResponse<String> apiResponse = new TaskDto.ApiResponse<>();

        // Assert
        assertThat(createRequest).isNotNull();
        assertThat(updateRequest).isNotNull();
        assertThat(response).isNotNull();
        assertThat(apiResponse).isNotNull();
    }
}

