package com.wellness360.taskmanagement.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for AuthDto classes — covers LoginRequest, RegisterRequest, and LoginResponse.
 * Tests validation constraints and data integrity.
 */
@DisplayName("AuthDto Tests")
class AuthDtoTest {

    // ─────────────────────────────────────────────────────────────────────────
    // ────────────────── LOGIN REQUEST DTO TESTS ──────────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("LoginRequest: should create instance with valid username and password")
    void loginRequest_WithValidCredentials_ShouldCreateInstance() {
        // Arrange & Act
        AuthDto.LoginRequest loginRequest = new AuthDto.LoginRequest("testuser", "password123");

        // Assert
        assertThat(loginRequest).isNotNull();
        assertThat(loginRequest.getUsername()).isEqualTo("testuser");
        assertThat(loginRequest.getPassword()).isEqualTo("password123");
    }

    @Test
    @DisplayName("LoginRequest: should have default constructor (no-arg)")
    void loginRequest_ShouldHaveNoArgConstructor() {
        // Arrange & Act
        AuthDto.LoginRequest loginRequest = new AuthDto.LoginRequest();

        // Assert
        assertThat(loginRequest).isNotNull();
        assertThat(loginRequest.getUsername()).isNull();
        assertThat(loginRequest.getPassword()).isNull();
    }

    @Test
    @DisplayName("LoginRequest: should set username via setter")
    void loginRequest_SetUsername_ShouldUpdateField() {
        // Arrange
        AuthDto.LoginRequest loginRequest = new AuthDto.LoginRequest();

        // Act
        loginRequest.setUsername("newuser");

        // Assert
        assertThat(loginRequest.getUsername()).isEqualTo("newuser");
    }

    @Test
    @DisplayName("LoginRequest: should set password via setter")
    void loginRequest_SetPassword_ShouldUpdateField() {
        // Arrange
        AuthDto.LoginRequest loginRequest = new AuthDto.LoginRequest();

        // Act
        loginRequest.setPassword("newpassword");

        // Assert
        assertThat(loginRequest.getPassword()).isEqualTo("newpassword");
    }

    @Test
    @DisplayName("LoginRequest: should support getter and setter for username")
    void loginRequest_GetterSetter_ShouldWorkCorrectly() {
        // Arrange
        AuthDto.LoginRequest loginRequest = new AuthDto.LoginRequest("user1", "pass1");

        // Act
        String username = loginRequest.getUsername();
        loginRequest.setUsername("user2");

        // Assert
        assertThat(username).isEqualTo("user1");
        assertThat(loginRequest.getUsername()).isEqualTo("user2");
    }

    @Test
    @DisplayName("LoginRequest: should support getter and setter for password")
    void loginRequest_GetterSetter_PasswordShouldWorkCorrectly() {
        // Arrange
        AuthDto.LoginRequest loginRequest = new AuthDto.LoginRequest("user", "pass1");

        // Act
        String password = loginRequest.getPassword();
        loginRequest.setPassword("pass2");

        // Assert
        assertThat(password).isEqualTo("pass1");
        assertThat(loginRequest.getPassword()).isEqualTo("pass2");
    }

    @Test
    @DisplayName("LoginRequest: should handle special characters in username")
    void loginRequest_WithSpecialCharactersInUsername_ShouldStore() {
        // Arrange & Act
        AuthDto.LoginRequest loginRequest = new AuthDto.LoginRequest("user@example.com", "pass");

        // Assert
        assertThat(loginRequest.getUsername()).isEqualTo("user@example.com");
    }

    @Test
    @DisplayName("LoginRequest: should handle special characters in password")
    void loginRequest_WithSpecialCharactersInPassword_ShouldStore() {
        // Arrange & Act
        AuthDto.LoginRequest loginRequest = new AuthDto.LoginRequest("user", "P@ss!W0rd#2024");

        // Assert
        assertThat(loginRequest.getPassword()).isEqualTo("P@ss!W0rd#2024");
    }

    @Test
    @DisplayName("LoginRequest: should handle empty strings")
    void loginRequest_WithEmptyStrings_ShouldStore() {
        // Arrange & Act
        AuthDto.LoginRequest loginRequest = new AuthDto.LoginRequest("", "");

        // Assert
        assertThat(loginRequest.getUsername()).isEmpty();
        assertThat(loginRequest.getPassword()).isEmpty();
    }

    @Test
    @DisplayName("LoginRequest: should handle very long username")
    void loginRequest_WithLongUsername_ShouldStore() {
        // Arrange
        String longUsername = "a".repeat(500);

        // Act
        AuthDto.LoginRequest loginRequest = new AuthDto.LoginRequest(longUsername, "password");

        // Assert
        assertThat(loginRequest.getUsername()).isEqualTo(longUsername);
        assertThat(loginRequest.getUsername()).hasSize(500);
    }

    @Test
    @DisplayName("LoginRequest: should handle whitespace in username")
    void loginRequest_WithWhitespaceInUsername_ShouldStore() {
        // Arrange & Act
        AuthDto.LoginRequest loginRequest = new AuthDto.LoginRequest("user name with spaces", "password");

        // Assert
        assertThat(loginRequest.getUsername()).isEqualTo("user name with spaces");
    }

    @Test
    @DisplayName("LoginRequest: should allow null username")
    void loginRequest_WithNullUsername_ShouldStore() {
        // Arrange & Act
        AuthDto.LoginRequest loginRequest = new AuthDto.LoginRequest(null, "password");

        // Assert
        assertThat(loginRequest.getUsername()).isNull();
    }

    @Test
    @DisplayName("LoginRequest: should allow null password")
    void loginRequest_WithNullPassword_ShouldStore() {
        // Arrange & Act
        AuthDto.LoginRequest loginRequest = new AuthDto.LoginRequest("username", null);

        // Assert
        assertThat(loginRequest.getPassword()).isNull();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ──────────────── REGISTER REQUEST DTO TESTS ──────────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("RegisterRequest: should create instance with valid username and password")
    void registerRequest_WithValidData_ShouldCreateInstance() {
        // Arrange & Act
        AuthDto.RegisterRequest registerRequest = new AuthDto.RegisterRequest("newuser", "password123");

        // Assert
        assertThat(registerRequest).isNotNull();
        assertThat(registerRequest.getUsername()).isEqualTo("newuser");
        assertThat(registerRequest.getPassword()).isEqualTo("password123");
    }

    @Test
    @DisplayName("RegisterRequest: should have default constructor (no-arg)")
    void registerRequest_ShouldHaveNoArgConstructor() {
        // Arrange & Act
        AuthDto.RegisterRequest registerRequest = new AuthDto.RegisterRequest();

        // Assert
        assertThat(registerRequest).isNotNull();
        assertThat(registerRequest.getUsername()).isNull();
        assertThat(registerRequest.getPassword()).isNull();
    }

    @Test
    @DisplayName("RegisterRequest: should set username via setter")
    void registerRequest_SetUsername_ShouldUpdateField() {
        // Arrange
        AuthDto.RegisterRequest registerRequest = new AuthDto.RegisterRequest();

        // Act
        registerRequest.setUsername("alice");

        // Assert
        assertThat(registerRequest.getUsername()).isEqualTo("alice");
    }

    @Test
    @DisplayName("RegisterRequest: should set password via setter")
    void registerRequest_SetPassword_ShouldUpdateField() {
        // Arrange
        AuthDto.RegisterRequest registerRequest = new AuthDto.RegisterRequest();

        // Act
        registerRequest.setPassword("securepass");

        // Assert
        assertThat(registerRequest.getPassword()).isEqualTo("securepass");
    }

    @Test
    @DisplayName("RegisterRequest: should support getter and setter for username")
    void registerRequest_GetterSetter_UsernameShouldWorkCorrectly() {
        // Arrange
        AuthDto.RegisterRequest registerRequest = new AuthDto.RegisterRequest("user1", "pass1");

        // Act
        String username = registerRequest.getUsername();
        registerRequest.setUsername("user2");

        // Assert
        assertThat(username).isEqualTo("user1");
        assertThat(registerRequest.getUsername()).isEqualTo("user2");
    }

    @Test
    @DisplayName("RegisterRequest: should support getter and setter for password")
    void registerRequest_GetterSetter_PasswordShouldWorkCorrectly() {
        // Arrange
        AuthDto.RegisterRequest registerRequest = new AuthDto.RegisterRequest("user", "pass1");

        // Act
        String password = registerRequest.getPassword();
        registerRequest.setPassword("pass2");

        // Assert
        assertThat(password).isEqualTo("pass1");
        assertThat(registerRequest.getPassword()).isEqualTo("pass2");
    }

    @Test
    @DisplayName("RegisterRequest: should handle special characters in username")
    void registerRequest_WithSpecialCharactersInUsername_ShouldStore() {
        // Arrange & Act
        AuthDto.RegisterRequest registerRequest = new AuthDto.RegisterRequest("user.name_123@domain", "pass");

        // Assert
        assertThat(registerRequest.getUsername()).isEqualTo("user.name_123@domain");
    }

    @Test
    @DisplayName("RegisterRequest: should handle special characters in password")
    void registerRequest_WithSpecialCharactersInPassword_ShouldStore() {
        // Arrange & Act
        AuthDto.RegisterRequest registerRequest = new AuthDto.RegisterRequest("user", "P@$$w0rd!#%&*");

        // Assert
        assertThat(registerRequest.getPassword()).isEqualTo("P@$$w0rd!#%&*");
    }

    @Test
    @DisplayName("RegisterRequest: should handle empty strings")
    void registerRequest_WithEmptyStrings_ShouldStore() {
        // Arrange & Act
        AuthDto.RegisterRequest registerRequest = new AuthDto.RegisterRequest("", "");

        // Assert
        assertThat(registerRequest.getUsername()).isEmpty();
        assertThat(registerRequest.getPassword()).isEmpty();
    }

    @Test
    @DisplayName("RegisterRequest: should handle very long username")
    void registerRequest_WithLongUsername_ShouldStore() {
        // Arrange
        String longUsername = "user_".repeat(100);

        // Act
        AuthDto.RegisterRequest registerRequest = new AuthDto.RegisterRequest(longUsername, "password");

        // Assert
        assertThat(registerRequest.getUsername()).isEqualTo(longUsername);
    }

    @Test
    @DisplayName("RegisterRequest: should allow null username")
    void registerRequest_WithNullUsername_ShouldStore() {
        // Arrange & Act
        AuthDto.RegisterRequest registerRequest = new AuthDto.RegisterRequest(null, "password");

        // Assert
        assertThat(registerRequest.getUsername()).isNull();
    }

    @Test
    @DisplayName("RegisterRequest: should allow null password")
    void registerRequest_WithNullPassword_ShouldStore() {
        // Arrange & Act
        AuthDto.RegisterRequest registerRequest = new AuthDto.RegisterRequest("username", null);

        // Assert
        assertThat(registerRequest.getPassword()).isNull();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ──────────────── LOGIN RESPONSE DTO TESTS ──────────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("LoginResponse: should create instance with builder pattern")
    void loginResponse_WithBuilder_ShouldCreateInstance() {
        // Arrange & Act
        AuthDto.LoginResponse loginResponse = AuthDto.LoginResponse.builder()
                .token("jwt-token-123")
                .username("testuser")
                .message("Login successful")
                .build();

        // Assert
        assertThat(loginResponse).isNotNull();
        assertThat(loginResponse.getToken()).isEqualTo("jwt-token-123");
        assertThat(loginResponse.getUsername()).isEqualTo("testuser");
        assertThat(loginResponse.getMessage()).isEqualTo("Login successful");
    }

    @Test
    @DisplayName("LoginResponse: should have default constructor (no-arg)")
    void loginResponse_ShouldHaveNoArgConstructor() {
        // Arrange & Act
        AuthDto.LoginResponse loginResponse = new AuthDto.LoginResponse();

        // Assert
        assertThat(loginResponse).isNotNull();
        assertThat(loginResponse.getToken()).isNull();
        assertThat(loginResponse.getUsername()).isNull();
        assertThat(loginResponse.getMessage()).isNull();
    }

    @Test
    @DisplayName("LoginResponse: should create instance with all-arg constructor")
    void loginResponse_WithAllArgConstructor_ShouldCreateInstance() {
        // Arrange & Act
        AuthDto.LoginResponse loginResponse = new AuthDto.LoginResponse(
                "token123", "user123", "Success"
        );

        // Assert
        assertThat(loginResponse.getToken()).isEqualTo("token123");
        assertThat(loginResponse.getUsername()).isEqualTo("user123");
        assertThat(loginResponse.getMessage()).isEqualTo("Success");
    }

    @Test
    @DisplayName("LoginResponse: should set token via setter")
    void loginResponse_SetToken_ShouldUpdateField() {
        // Arrange
        AuthDto.LoginResponse loginResponse = new AuthDto.LoginResponse();

        // Act
        loginResponse.setToken("new-token");

        // Assert
        assertThat(loginResponse.getToken()).isEqualTo("new-token");
    }

    @Test
    @DisplayName("LoginResponse: should set username via setter")
    void loginResponse_SetUsername_ShouldUpdateField() {
        // Arrange
        AuthDto.LoginResponse loginResponse = new AuthDto.LoginResponse();

        // Act
        loginResponse.setUsername("newuser");

        // Assert
        assertThat(loginResponse.getUsername()).isEqualTo("newuser");
    }

    @Test
    @DisplayName("LoginResponse: should set message via setter")
    void loginResponse_SetMessage_ShouldUpdateField() {
        // Arrange
        AuthDto.LoginResponse loginResponse = new AuthDto.LoginResponse();

        // Act
        loginResponse.setMessage("Updated message");

        // Assert
        assertThat(loginResponse.getMessage()).isEqualTo("Updated message");
    }

    @Test
    @DisplayName("LoginResponse: should support getter and setter for token")
    void loginResponse_GetterSetter_TokenShouldWorkCorrectly() {
        // Arrange
        AuthDto.LoginResponse loginResponse = AuthDto.LoginResponse.builder()
                .token("token1")
                .build();

        // Act
        String token = loginResponse.getToken();
        loginResponse.setToken("token2");

        // Assert
        assertThat(token).isEqualTo("token1");
        assertThat(loginResponse.getToken()).isEqualTo("token2");
    }

    @Test
    @DisplayName("LoginResponse: should support getter and setter for username")
    void loginResponse_GetterSetter_UsernameShouldWorkCorrectly() {
        // Arrange
        AuthDto.LoginResponse loginResponse = AuthDto.LoginResponse.builder()
                .username("user1")
                .build();

        // Act
        String username = loginResponse.getUsername();
        loginResponse.setUsername("user2");

        // Assert
        assertThat(username).isEqualTo("user1");
        assertThat(loginResponse.getUsername()).isEqualTo("user2");
    }

    @Test
    @DisplayName("LoginResponse: should support getter and setter for message")
    void loginResponse_GetterSetter_MessageShouldWorkCorrectly() {
        // Arrange
        AuthDto.LoginResponse loginResponse = AuthDto.LoginResponse.builder()
                .message("Message1")
                .build();

        // Act
        String message = loginResponse.getMessage();
        loginResponse.setMessage("Message2");

        // Assert
        assertThat(message).isEqualTo("Message1");
        assertThat(loginResponse.getMessage()).isEqualTo("Message2");
    }

    @Test
    @DisplayName("LoginResponse: builder should allow partial configuration")
    void loginResponse_BuilderPartial_ShouldCreateWithNullFields() {
        // Arrange & Act
        AuthDto.LoginResponse loginResponse = AuthDto.LoginResponse.builder()
                .token("token123")
                .build();

        // Assert
        assertThat(loginResponse.getToken()).isEqualTo("token123");
        assertThat(loginResponse.getUsername()).isNull();
        assertThat(loginResponse.getMessage()).isNull();
    }

    @Test
    @DisplayName("LoginResponse: should handle very long JWT token")
    void loginResponse_WithLongToken_ShouldStore() {
        // Arrange
        String longToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." +
                "eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ." +
                "SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c_".repeat(10);

        // Act
        AuthDto.LoginResponse loginResponse = AuthDto.LoginResponse.builder()
                .token(longToken)
                .build();

        // Assert
        assertThat(loginResponse.getToken()).isEqualTo(longToken);
    }

    @Test
    @DisplayName("LoginResponse: should handle special characters in message")
    void loginResponse_WithSpecialCharactersInMessage_ShouldStore() {
        // Arrange & Act
        AuthDto.LoginResponse loginResponse = AuthDto.LoginResponse.builder()
                .message("Login successful! @#$%^&*()")
                .build();

        // Assert
        assertThat(loginResponse.getMessage()).isEqualTo("Login successful! @#$%^&*()");
    }

    @Test
    @DisplayName("LoginResponse: should allow null token")
    void loginResponse_WithNullToken_ShouldStore() {
        // Arrange & Act
        AuthDto.LoginResponse loginResponse = AuthDto.LoginResponse.builder()
                .token(null)
                .username("user")
                .build();

        // Assert
        assertThat(loginResponse.getToken()).isNull();
        assertThat(loginResponse.getUsername()).isEqualTo("user");
    }

    @Test
    @DisplayName("LoginResponse: should allow null username")
    void loginResponse_WithNullUsername_ShouldStore() {
        // Arrange & Act
        AuthDto.LoginResponse loginResponse = AuthDto.LoginResponse.builder()
                .token("token")
                .username(null)
                .build();

        // Assert
        assertThat(loginResponse.getToken()).isEqualTo("token");
        assertThat(loginResponse.getUsername()).isNull();
    }

    @Test
    @DisplayName("LoginResponse: should allow null message")
    void loginResponse_WithNullMessage_ShouldStore() {
        // Arrange & Act
        AuthDto.LoginResponse loginResponse = AuthDto.LoginResponse.builder()
                .token("token")
                .message(null)
                .build();

        // Assert
        assertThat(loginResponse.getToken()).isEqualTo("token");
        assertThat(loginResponse.getMessage()).isNull();
    }

    @Test
    @DisplayName("LoginResponse: should build with all fields set to null")
    void loginResponse_BuilderAllNull_ShouldCreateInstance() {
        // Arrange & Act
        AuthDto.LoginResponse loginResponse = AuthDto.LoginResponse.builder()
                .token(null)
                .username(null)
                .message(null)
                .build();

        // Assert
        assertThat(loginResponse).isNotNull();
        assertThat(loginResponse.getToken()).isNull();
        assertThat(loginResponse.getUsername()).isNull();
        assertThat(loginResponse.getMessage()).isNull();
    }

    @Test
    @DisplayName("LoginResponse: builder should work with multiple builds")
    void loginResponse_MultipleBuildCalls_ShouldCreateIndependentInstances() {
        // Arrange & Act
        AuthDto.LoginResponse response1 = AuthDto.LoginResponse.builder()
                .token("token1")
                .username("user1")
                .message("Message1")
                .build();

        AuthDto.LoginResponse response2 = AuthDto.LoginResponse.builder()
                .token("token2")
                .username("user2")
                .message("Message2")
                .build();

        // Assert
        assertThat(response1.getToken()).isEqualTo("token1");
        assertThat(response2.getToken()).isEqualTo("token2");
        assertThat(response1.getUsername()).isEqualTo("user1");
        assertThat(response2.getUsername()).isEqualTo("user2");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ────────────────── CROSS-DTO COMPARISON TESTS ──────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("LoginRequest and RegisterRequest should be independent")
    void loginAndRegisterRequest_ShouldBeIndependent() {
        // Arrange & Act
        AuthDto.LoginRequest loginRequest = new AuthDto.LoginRequest("testuser", "password123");
        AuthDto.RegisterRequest registerRequest = new AuthDto.RegisterRequest("newuser", "password456");

        // Assert
        assertThat(loginRequest.getUsername()).isNotEqualTo(registerRequest.getUsername());
        assertThat(loginRequest.getPassword()).isNotEqualTo(registerRequest.getPassword());
    }

    @Test
    @DisplayName("LoginResponse should store credentials from both requests")
    void loginResponse_CanStoreDataFromBothRequests() {
        // Arrange
        AuthDto.LoginRequest loginRequest = new AuthDto.LoginRequest("testuser", "password");
        AuthDto.RegisterRequest registerRequest = new AuthDto.RegisterRequest("newuser", "password");

        // Act
        AuthDto.LoginResponse response1 = AuthDto.LoginResponse.builder()
                .username(loginRequest.getUsername())
                .token("token1")
                .build();

        AuthDto.LoginResponse response2 = AuthDto.LoginResponse.builder()
                .username(registerRequest.getUsername())
                .token("token2")
                .build();

        // Assert
        assertThat(response1.getUsername()).isEqualTo("testuser");
        assertThat(response2.getUsername()).isEqualTo("newuser");
    }

    @Test
    @DisplayName("All DTOs should be instantiable independently")
    void allDtos_ShouldBeInstantiableIndependently() {
        // Arrange & Act
        AuthDto.LoginRequest loginRequest = new AuthDto.LoginRequest("user", "pass");
        AuthDto.RegisterRequest registerRequest = new AuthDto.RegisterRequest("user", "pass");
        AuthDto.LoginResponse loginResponse = AuthDto.LoginResponse.builder()
                .token("token")
                .build();

        // Assert
        assertThat(loginRequest).isNotNull();
        assertThat(registerRequest).isNotNull();
        assertThat(loginResponse).isNotNull();
    }
}

