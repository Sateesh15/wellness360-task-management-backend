package com.wellness360.taskmanagement.exception;

import com.wellness360.taskmanagement.dto.TaskDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for GlobalExceptionHandler — covers all exception handling scenarios.
 * Tests proper HTTP status codes, error messages, and response format consistency.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("GlobalExceptionHandler Tests")
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private BindingResult bindingResult;


    // ─────────────────────────────────────────────────────────────────────────
    // ──────────────── TASK NOT FOUND EXCEPTION TESTS ──────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("handleTaskNotFound: should return 404 status for TaskNotFoundException")
    void handleTaskNotFound_ShouldReturn404Status() {
        // Arrange
        TaskNotFoundException exception = new TaskNotFoundException("task-123");

        // Act
        ResponseEntity<TaskDto.ApiResponse<Void>> response = globalExceptionHandler.handleTaskNotFound(exception);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }

    @Test
    @DisplayName("handleTaskNotFound: should return error response with correct message")
    void handleTaskNotFound_ShouldReturnErrorMessage() {
        // Arrange
        String taskId = "task-456";
        TaskNotFoundException exception = new TaskNotFoundException(taskId);

        // Act
        ResponseEntity<TaskDto.ApiResponse<Void>> response = globalExceptionHandler.handleTaskNotFound(exception);

        // Assert
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getMessage()).contains(taskId);
    }

    @Test
    @DisplayName("handleTaskNotFound: should set success flag to false")
    void handleTaskNotFound_ShouldSetSuccessFalse() {
        // Arrange
        TaskNotFoundException exception = new TaskNotFoundException("task-789");

        // Act
        ResponseEntity<TaskDto.ApiResponse<Void>> response = globalExceptionHandler.handleTaskNotFound(exception);

        // Assert
        assertThat(response.getBody().isSuccess()).isFalse();
    }

    @Test
    @DisplayName("handleTaskNotFound: should have null data in response")
    void handleTaskNotFound_ShouldHaveNullData() {
        // Arrange
        TaskNotFoundException exception = new TaskNotFoundException("invalid-task");

        // Act
        ResponseEntity<TaskDto.ApiResponse<Void>> response = globalExceptionHandler.handleTaskNotFound(exception);

        // Assert
        assertThat(response.getBody().getData()).isNull();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ──────────── VALIDATION ERRORS EXCEPTION TESTS ───────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("handleValidationErrors: should return 400 status for validation errors")
    void handleValidationErrors_ShouldReturn400Status() {
        // Arrange
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<TaskDto.ApiResponse<Map<String, String>>> response =
                globalExceptionHandler.handleValidationErrors(exception);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    @DisplayName("handleValidationErrors: should return validation failed message")
    void handleValidationErrors_ShouldReturnValidationFailedMessage() {
        // Arrange
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<TaskDto.ApiResponse<Map<String, String>>> response =
                globalExceptionHandler.handleValidationErrors(exception);

        // Assert
        assertThat(response.getBody().getMessage()).isEqualTo("Validation failed");
    }

    @Test
    @DisplayName("handleValidationErrors: should set success flag to false")
    void handleValidationErrors_ShouldSetSuccessFalse() {
        // Arrange
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<TaskDto.ApiResponse<Map<String, String>>> response =
                globalExceptionHandler.handleValidationErrors(exception);

        // Assert
        assertThat(response.getBody().isSuccess()).isFalse();
    }

    @Test
    @DisplayName("handleValidationErrors: should include field errors in response data")
    void handleValidationErrors_ShouldIncludeFieldErrors() {
        // Arrange
        FieldError fieldError = new FieldError("taskRequest", "title", "Title is required");
        List<org.springframework.validation.ObjectError> errors = new ArrayList<>();
        errors.add(fieldError);

        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(errors);

        // Act
        ResponseEntity<TaskDto.ApiResponse<Map<String, String>>> response =
                globalExceptionHandler.handleValidationErrors(exception);

        // Assert
        assertThat(response.getBody().getData()).isNotEmpty();
        assertThat(response.getBody().getData()).containsEntry("title", "Title is required");
    }

    @Test
    @DisplayName("handleValidationErrors: should handle multiple field errors")
    void handleValidationErrors_WithMultipleErrors_ShouldIncludeAll() {
        // Arrange
        List<org.springframework.validation.ObjectError> errors = new ArrayList<>();
        errors.add(new FieldError("request", "title", "Title is required"));
        errors.add(new FieldError("request", "dueDate", "Due date must be in future"));
        errors.add(new FieldError("request", "description", "Description too long"));

        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(errors);

        // Act
        ResponseEntity<TaskDto.ApiResponse<Map<String, String>>> response =
                globalExceptionHandler.handleValidationErrors(exception);

        // Assert
        assertThat(response.getBody().getData()).hasSize(3);
        assertThat(response.getBody().getData()).containsKeys("title", "dueDate", "description");
    }

    @Test
    @DisplayName("handleValidationErrors: should return empty map for no errors")
    void handleValidationErrors_WithNoErrors_ShouldReturnEmptyMap() {
        // Arrange
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<TaskDto.ApiResponse<Map<String, String>>> response =
                globalExceptionHandler.handleValidationErrors(exception);

        // Assert
        assertThat(response.getBody().getData()).isEmpty();
    }

    @Test
    @DisplayName("handleValidationErrors: should extract error messages correctly")
    void handleValidationErrors_ShouldExtractErrorMessagesCorrectly() {
        // Arrange
        List<org.springframework.validation.ObjectError> errors = new ArrayList<>();
        errors.add(new FieldError("createRequest", "title", "Title must be between 1 and 200 characters"));
        errors.add(new FieldError("createRequest", "dueDate", "Due date must be today or in the future"));

        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(errors);

        // Act
        ResponseEntity<TaskDto.ApiResponse<Map<String, String>>> response =
                globalExceptionHandler.handleValidationErrors(exception);

        // Assert
        Map<String, String> data = response.getBody().getData();
        assertThat(data.get("title")).isEqualTo("Title must be between 1 and 200 characters");
        assertThat(data.get("dueDate")).isEqualTo("Due date must be today or in the future");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ──────────── ILLEGAL ARGUMENT EXCEPTION TESTS ──────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("handleIllegalArgument: should return 400 status for IllegalArgumentException")
    void handleIllegalArgument_ShouldReturn400Status() {
        // Arrange
        IllegalArgumentException exception = new IllegalArgumentException("Invalid username or password");

        // Act
        ResponseEntity<TaskDto.ApiResponse<Void>> response =
                globalExceptionHandler.handleIllegalArgument(exception);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    @DisplayName("handleIllegalArgument: should return error message from exception")
    void handleIllegalArgument_ShouldReturnExceptionMessage() {
        // Arrange
        String message = "Username already exists";
        IllegalArgumentException exception = new IllegalArgumentException(message);

        // Act
        ResponseEntity<TaskDto.ApiResponse<Void>> response =
                globalExceptionHandler.handleIllegalArgument(exception);

        // Assert
        assertThat(response.getBody().getMessage()).isEqualTo(message);
    }

    @Test
    @DisplayName("handleIllegalArgument: should set success flag to false")
    void handleIllegalArgument_ShouldSetSuccessFalse() {
        // Arrange
        IllegalArgumentException exception = new IllegalArgumentException("Invalid input");

        // Act
        ResponseEntity<TaskDto.ApiResponse<Void>> response =
                globalExceptionHandler.handleIllegalArgument(exception);

        // Assert
        assertThat(response.getBody().isSuccess()).isFalse();
    }

    @Test
    @DisplayName("handleIllegalArgument: should have null data in response")
    void handleIllegalArgument_ShouldHaveNullData() {
        // Arrange
        IllegalArgumentException exception = new IllegalArgumentException("Bad argument");

        // Act
        ResponseEntity<TaskDto.ApiResponse<Void>> response =
                globalExceptionHandler.handleIllegalArgument(exception);

        // Assert
        assertThat(response.getBody().getData()).isNull();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ──────────── TYPE MISMATCH EXCEPTION TESTS ──────────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("handleTypeMismatch: should return 400 status for MethodArgumentTypeMismatchException")
    void handleTypeMismatch_ShouldReturn400Status() {
        // Arrange
        MethodArgumentTypeMismatchException exception = mock(MethodArgumentTypeMismatchException.class);
        when(exception.getName()).thenReturn("id");

        // Act
        ResponseEntity<TaskDto.ApiResponse<Void>> response =
                globalExceptionHandler.handleTypeMismatch(exception);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    @DisplayName("handleTypeMismatch: should include parameter name in error message")
    void handleTypeMismatch_ShouldIncludeParameterName() {
        // Arrange
        MethodArgumentTypeMismatchException exception = mock(MethodArgumentTypeMismatchException.class);
        when(exception.getName()).thenReturn("taskId");

        // Act
        ResponseEntity<TaskDto.ApiResponse<Void>> response =
                globalExceptionHandler.handleTypeMismatch(exception);

        // Assert
        assertThat(response.getBody().getMessage()).contains("taskId");
    }

    @Test
    @DisplayName("handleTypeMismatch: should set success flag to false")
    void handleTypeMismatch_ShouldSetSuccessFalse() {
        // Arrange
        MethodArgumentTypeMismatchException exception = mock(MethodArgumentTypeMismatchException.class);
        when(exception.getName()).thenReturn("id");

        // Act
        ResponseEntity<TaskDto.ApiResponse<Void>> response =
                globalExceptionHandler.handleTypeMismatch(exception);

        // Assert
        assertThat(response.getBody().isSuccess()).isFalse();
    }

    @Test
    @DisplayName("handleTypeMismatch: should have null data in response")
    void handleTypeMismatch_ShouldHaveNullData() {
        // Arrange
        MethodArgumentTypeMismatchException exception = mock(MethodArgumentTypeMismatchException.class);
        when(exception.getName()).thenReturn("id");

        // Act
        ResponseEntity<TaskDto.ApiResponse<Void>> response =
                globalExceptionHandler.handleTypeMismatch(exception);

        // Assert
        assertThat(response.getBody().getData()).isNull();
    }

    @Test
    @DisplayName("handleTypeMismatch: should format message as 'Invalid parameter: {name}'")
    void handleTypeMismatch_ShouldFormatMessageCorrectly() {
        // Arrange
        MethodArgumentTypeMismatchException exception = mock(MethodArgumentTypeMismatchException.class);
        when(exception.getName()).thenReturn("userId");

        // Act
        ResponseEntity<TaskDto.ApiResponse<Void>> response =
                globalExceptionHandler.handleTypeMismatch(exception);

        // Assert
        assertThat(response.getBody().getMessage()).isEqualTo("Invalid parameter: userId");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ────────────── BAD CREDENTIALS EXCEPTION TESTS ──────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("handleBadCredentials: should return 401 status for BadCredentialsException")
    void handleBadCredentials_ShouldReturn401Status() {
        // Arrange
        BadCredentialsException exception = new BadCredentialsException("Bad credentials");

        // Act
        ResponseEntity<TaskDto.ApiResponse<Void>> response =
                globalExceptionHandler.handleBadCredentials(exception);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getStatusCode().value()).isEqualTo(401);
    }

    @Test
    @DisplayName("handleBadCredentials: should return 'Invalid username or password' message")
    void handleBadCredentials_ShouldReturnUnauthorizedMessage() {
        // Arrange
        BadCredentialsException exception = new BadCredentialsException("Invalid credentials");

        // Act
        ResponseEntity<TaskDto.ApiResponse<Void>> response =
                globalExceptionHandler.handleBadCredentials(exception);

        // Assert
        assertThat(response.getBody().getMessage()).isEqualTo("Invalid username or password");
    }

    @Test
    @DisplayName("handleBadCredentials: should set success flag to false")
    void handleBadCredentials_ShouldSetSuccessFalse() {
        // Arrange
        BadCredentialsException exception = new BadCredentialsException("Authentication failed");

        // Act
        ResponseEntity<TaskDto.ApiResponse<Void>> response =
                globalExceptionHandler.handleBadCredentials(exception);

        // Assert
        assertThat(response.getBody().isSuccess()).isFalse();
    }

    @Test
    @DisplayName("handleBadCredentials: should have null data in response")
    void handleBadCredentials_ShouldHaveNullData() {
        // Arrange
        BadCredentialsException exception = new BadCredentialsException("Wrong password");

        // Act
        ResponseEntity<TaskDto.ApiResponse<Void>> response =
                globalExceptionHandler.handleBadCredentials(exception);

        // Assert
        assertThat(response.getBody().getData()).isNull();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ────────────── GENERAL EXCEPTION TESTS ──────────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("handleGeneral: should return 500 status for generic Exception")
    void handleGeneral_ShouldReturn500Status() {
        // Arrange
        Exception exception = new Exception("Something went wrong");

        // Act
        ResponseEntity<TaskDto.ApiResponse<Void>> response =
                globalExceptionHandler.handleGeneral(exception);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getStatusCode().value()).isEqualTo(500);
    }

    @Test
    @DisplayName("handleGeneral: should include exception message in response")
    void handleGeneral_ShouldIncludeExceptionMessage() {
        // Arrange
        String message = "Database connection failed";
        Exception exception = new Exception(message);

        // Act
        ResponseEntity<TaskDto.ApiResponse<Void>> response =
                globalExceptionHandler.handleGeneral(exception);

        // Assert
        assertThat(response.getBody().getMessage()).contains(message);
    }

    @Test
    @DisplayName("handleGeneral: should set success flag to false")
    void handleGeneral_ShouldSetSuccessFalse() {
        // Arrange
        Exception exception = new Exception("Internal error");

        // Act
        ResponseEntity<TaskDto.ApiResponse<Void>> response =
                globalExceptionHandler.handleGeneral(exception);

        // Assert
        assertThat(response.getBody().isSuccess()).isFalse();
    }

    @Test
    @DisplayName("handleGeneral: should have null data in response")
    void handleGeneral_ShouldHaveNullData() {
        // Arrange
        Exception exception = new Exception("Server error");

        // Act
        ResponseEntity<TaskDto.ApiResponse<Void>> response =
                globalExceptionHandler.handleGeneral(exception);

        // Assert
        assertThat(response.getBody().getData()).isNull();
    }

    @Test
    @DisplayName("handleGeneral: should include 'An unexpected error occurred' prefix")
    void handleGeneral_ShouldIncludeErrorPrefix() {
        // Arrange
        Exception exception = new Exception("Null pointer exception");

        // Act
        ResponseEntity<TaskDto.ApiResponse<Void>> response =
                globalExceptionHandler.handleGeneral(exception);

        // Assert
        assertThat(response.getBody().getMessage()).startsWith("An unexpected error occurred:");
    }

    @Test
    @DisplayName("handleGeneral: should handle null exception message")
    void handleGeneral_WithNullMessage_ShouldHandleGracefully() {
        // Arrange
        Exception exception = new Exception((String) null);

        // Act
        ResponseEntity<TaskDto.ApiResponse<Void>> response =
                globalExceptionHandler.handleGeneral(exception);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @DisplayName("handleGeneral: should handle RuntimeException")
    void handleGeneral_WithRuntimeException_ShouldHandle() {
        // Arrange
        RuntimeException exception = new RuntimeException("Runtime error occurred");

        // Act
        ResponseEntity<TaskDto.ApiResponse<Void>> response =
                globalExceptionHandler.handleGeneral(exception);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().isSuccess()).isFalse();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ──────────────── CROSS-EXCEPTION CONSISTENCY TESTS ──────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("All exception handlers should return ApiResponse with consistent structure")
    void allExceptionHandlers_ShouldReturnConsistentStructure() {
        // Arrange
        TaskNotFoundException taskEx = new TaskNotFoundException("task-1");
        IllegalArgumentException illegalEx = new IllegalArgumentException("Invalid");
        BadCredentialsException credEx = new BadCredentialsException("Bad creds");
        Exception generalEx = new Exception("General error");

        // Act
        ResponseEntity<TaskDto.ApiResponse<Void>> taskResponse = globalExceptionHandler.handleTaskNotFound(taskEx);
        ResponseEntity<TaskDto.ApiResponse<Void>> illegalResponse = globalExceptionHandler.handleIllegalArgument(illegalEx);
        ResponseEntity<TaskDto.ApiResponse<Void>> credResponse = globalExceptionHandler.handleBadCredentials(credEx);
        ResponseEntity<TaskDto.ApiResponse<Void>> generalResponse = globalExceptionHandler.handleGeneral(generalEx);

        // Assert - All should have ApiResponse structure
        assertThat(taskResponse.getBody()).isNotNull();
        assertThat(illegalResponse.getBody()).isNotNull();
        assertThat(credResponse.getBody()).isNotNull();
        assertThat(generalResponse.getBody()).isNotNull();

        // All should have success flag and message
        assertThat(taskResponse.getBody().isSuccess()).isFalse();
        assertThat(illegalResponse.getBody().isSuccess()).isFalse();
        assertThat(credResponse.getBody().isSuccess()).isFalse();
        assertThat(generalResponse.getBody().isSuccess()).isFalse();
    }

    @Test
    @DisplayName("Exception handlers should return appropriate HTTP status codes")
    void exceptionHandlers_ShouldReturnCorrectStatusCodes() {
        // Arrange
        TaskNotFoundException taskEx = new TaskNotFoundException("task-1");
        IllegalArgumentException illegalEx = new IllegalArgumentException("Invalid");
        BadCredentialsException credEx = new BadCredentialsException("Bad creds");
        Exception generalEx = new Exception("General error");

        // Act
        ResponseEntity<TaskDto.ApiResponse<Void>> taskResponse = globalExceptionHandler.handleTaskNotFound(taskEx);
        ResponseEntity<TaskDto.ApiResponse<Void>> illegalResponse = globalExceptionHandler.handleIllegalArgument(illegalEx);
        ResponseEntity<TaskDto.ApiResponse<Void>> credResponse = globalExceptionHandler.handleBadCredentials(credEx);
        ResponseEntity<TaskDto.ApiResponse<Void>> generalResponse = globalExceptionHandler.handleGeneral(generalEx);

        // Assert
        assertThat(taskResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(illegalResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(credResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(generalResponse.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("All error responses should have success flag set to false")
    void allErrorResponses_ShouldHaveSuccessFalse() {
        // Arrange
        TaskNotFoundException taskEx = new TaskNotFoundException("task-1");
        IllegalArgumentException illegalEx = new IllegalArgumentException("Invalid");
        BadCredentialsException credEx = new BadCredentialsException("Bad");
        Exception generalEx = new Exception("Error");
        MethodArgumentNotValidException validationEx = mock(MethodArgumentNotValidException.class);
        when(validationEx.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(new ArrayList<>());

        // Act & Assert
        assertThat(globalExceptionHandler.handleTaskNotFound(taskEx).getBody().isSuccess()).isFalse();
        assertThat(globalExceptionHandler.handleIllegalArgument(illegalEx).getBody().isSuccess()).isFalse();
        assertThat(globalExceptionHandler.handleBadCredentials(credEx).getBody().isSuccess()).isFalse();
        assertThat(globalExceptionHandler.handleGeneral(generalEx).getBody().isSuccess()).isFalse();
        assertThat(globalExceptionHandler.handleValidationErrors(validationEx).getBody().isSuccess()).isFalse();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ──────────────── EDGE CASES & VALIDATION TESTS ────────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("handleTaskNotFound: should handle exception with empty message")
    void handleTaskNotFound_WithEmptyMessage_ShouldHandle() {
        // Arrange
        TaskNotFoundException exception = new TaskNotFoundException("");

        // Act
        ResponseEntity<TaskDto.ApiResponse<Void>> response = globalExceptionHandler.handleTaskNotFound(exception);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @DisplayName("handleIllegalArgument: should handle exception with very long message")
    void handleIllegalArgument_WithLongMessage_ShouldHandle() {
        // Arrange
        String longMessage = "Error ".repeat(100);
        IllegalArgumentException exception = new IllegalArgumentException(longMessage);

        // Act
        ResponseEntity<TaskDto.ApiResponse<Void>> response =
                globalExceptionHandler.handleIllegalArgument(exception);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getMessage()).isEqualTo(longMessage);
    }

    @Test
    @DisplayName("handleValidationErrors: should handle non-FieldError objects")
    void handleValidationErrors_WithNonFieldErrors_ShouldExtractObjectName() {
        // Arrange
        org.springframework.validation.ObjectError nonFieldError =
                new org.springframework.validation.ObjectError("taskRequest", "Object validation failed");
        List<org.springframework.validation.ObjectError> errors = new ArrayList<>();
        errors.add(nonFieldError);

        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(errors);

        // Act
        ResponseEntity<TaskDto.ApiResponse<Map<String, String>>> response =
                globalExceptionHandler.handleValidationErrors(exception);

        // Assert
        assertThat(response.getBody().getData()).isNotEmpty();
        assertThat(response.getBody().getData()).containsKey("taskRequest");
    }

    @Test
    @DisplayName("handleTypeMismatch: should handle various parameter names")
    void handleTypeMismatch_WithDifferentParameterNames_ShouldIncludeParameterName() {
        // Arrange
        String[] parameterNames = {"id", "taskId", "userId", "pageNumber"};

        for (String paramName : parameterNames) {
            MethodArgumentTypeMismatchException exception = mock(MethodArgumentTypeMismatchException.class);
            when(exception.getName()).thenReturn(paramName);

            // Act
            ResponseEntity<TaskDto.ApiResponse<Void>> response =
                    globalExceptionHandler.handleTypeMismatch(exception);

            // Assert
            assertThat(response.getBody().getMessage()).contains(paramName);
        }
    }

    @Test
    @DisplayName("All exception handlers should have non-null response body")
    void allExceptionHandlers_ShouldHaveNonNullResponseBody() {
        // Arrange & Act & Assert
        assertThat(globalExceptionHandler.handleTaskNotFound(
                new TaskNotFoundException("test")).getBody()).isNotNull();
        assertThat(globalExceptionHandler.handleIllegalArgument(
                new IllegalArgumentException("test")).getBody()).isNotNull();
        assertThat(globalExceptionHandler.handleBadCredentials(
                new BadCredentialsException("test")).getBody()).isNotNull();
        assertThat(globalExceptionHandler.handleGeneral(
                new Exception("test")).getBody()).isNotNull();
    }

    @Test
    @DisplayName("Validation errors response should have Map<String, String> data type")
    void handleValidationErrors_ShouldReturnMapDataType() {
        // Arrange
        FieldError fieldError = new FieldError("request", "title", "Required");
        List<org.springframework.validation.ObjectError> errors = new ArrayList<>();
        errors.add(fieldError);

        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(errors);

        // Act
        ResponseEntity<TaskDto.ApiResponse<Map<String, String>>> response =
                globalExceptionHandler.handleValidationErrors(exception);

        // Assert
        assertThat(response.getBody().getData()).isInstanceOf(Map.class);
        assertThat(response.getBody().getData()).containsKey("title");
    }
}

