package com.wellness360.taskmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wellness360.taskmanagement.dto.AuthDto;
import com.wellness360.taskmanagement.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for AuthController — covers login and registration endpoints.
 * Uses MockMvc for HTTP layer testing and mocked AuthService.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AuthController Tests")
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ────────────────────── LOGIN ENDPOINT TESTS ──────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("POST /api/auth/login: should login successfully with valid credentials")
    void login_WithValidCredentials_ShouldReturnSuccess() throws Exception {
        // Arrange
        AuthDto.LoginRequest loginRequest = new AuthDto.LoginRequest("testuser", "password123");
        AuthDto.LoginResponse mockResponse = AuthDto.LoginResponse.builder()
                .token("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
                .username("testuser")
                .message("Login successful")
                .build();

        when(authService.login(any(AuthDto.LoginRequest.class))).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.data.token").value("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."))
                .andExpect(jsonPath("$.data.username").value("testuser"));

        verify(authService, times(1)).login(any(AuthDto.LoginRequest.class));
    }

    @Test
    @DisplayName("POST /api/auth/login: should return 400 when username is missing")
    void login_WithMissingUsername_ShouldReturnBadRequest() throws Exception {
        // Arrange
        AuthDto.LoginRequest loginRequest = new AuthDto.LoginRequest("", "password123");

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).login(any());
    }

    @Test
    @DisplayName("POST /api/auth/login: should return 400 when password is missing")
    void login_WithMissingPassword_ShouldReturnBadRequest() throws Exception {
        // Arrange
        AuthDto.LoginRequest loginRequest = new AuthDto.LoginRequest("testuser", "");

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).login(any());
    }

    @Test
    @DisplayName("POST /api/auth/login: should return 400 when both username and password are null")
    void login_WithNullCredentials_ShouldReturnBadRequest() throws Exception {
        // Arrange
        String requestBody = "{}";

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());

        verify(authService, never()).login(any());
    }

    @Test
    @DisplayName("POST /api/auth/login: should return valid JWT token in response")
    void login_ShouldReturnValidJwtToken() throws Exception {
        // Arrange
        AuthDto.LoginRequest loginRequest = new AuthDto.LoginRequest("user123", "secure_password");
        String jwtToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyMTIzIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

        AuthDto.LoginResponse mockResponse = AuthDto.LoginResponse.builder()
                .token(jwtToken)
                .username("user123")
                .message("Login successful")
                .build();

        when(authService.login(any(AuthDto.LoginRequest.class))).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").isNotEmpty())
                .andExpect(jsonPath("$.data.username").value("user123"));

        verify(authService, times(1)).login(any(AuthDto.LoginRequest.class));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ────────────────── REGISTRATION ENDPOINT TESTS ──────────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("POST /api/auth/register: should register a new user successfully")
    void register_WithValidData_ShouldReturnSuccess() throws Exception {
        // Arrange
        AuthDto.RegisterRequest registerRequest = new AuthDto.RegisterRequest("newuser", "password123");
        AuthDto.LoginResponse mockResponse = AuthDto.LoginResponse.builder()
                .token("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
                .username("newuser")
                .message("Registration successful")
                .build();

        when(authService.register(any(AuthDto.RegisterRequest.class))).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Registration successful"))
                .andExpect(jsonPath("$.data.token").value("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."))
                .andExpect(jsonPath("$.data.username").value("newuser"));

        verify(authService, times(1)).register(any(AuthDto.RegisterRequest.class));
    }

    @Test
    @DisplayName("POST /api/auth/register: should return 400 when username is missing")
    void register_WithMissingUsername_ShouldReturnBadRequest() throws Exception {
        // Arrange
        AuthDto.RegisterRequest registerRequest = new AuthDto.RegisterRequest("", "password123");

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).register(any());
    }

    @Test
    @DisplayName("POST /api/auth/register: should return 400 when password is missing")
    void register_WithMissingPassword_ShouldReturnBadRequest() throws Exception {
        // Arrange
        AuthDto.RegisterRequest registerRequest = new AuthDto.RegisterRequest("newuser", "");

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).register(any());
    }

    @Test
    @DisplayName("POST /api/auth/register: should return 400 when both fields are null")
    void register_WithNullData_ShouldReturnBadRequest() throws Exception {
        // Arrange
        String requestBody = "{}";

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());

        verify(authService, never()).register(any());
    }

    @Test
    @DisplayName("POST /api/auth/register: should generate JWT token after successful registration")
    void register_ShouldGenerateJwtToken() throws Exception {
        // Arrange
        AuthDto.RegisterRequest registerRequest = new AuthDto.RegisterRequest("alice", "secure_pass");
        String jwtToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhbGljZSIsImlhdCI6MTUxNjIzOTAyMn0.abcdef123456";

        AuthDto.LoginResponse mockResponse = AuthDto.LoginResponse.builder()
                .token(jwtToken)
                .username("alice")
                .message("Registration successful")
                .build();

        when(authService.register(any(AuthDto.RegisterRequest.class))).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").isNotEmpty())
                .andExpect(jsonPath("$.data.username").value("alice"));

        verify(authService, times(1)).register(any(AuthDto.RegisterRequest.class));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ──────────────────── EDGE CASES & VALIDATION TESTS ──────────────────────
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("POST /api/auth/login: should handle special characters in username")
    void login_WithSpecialCharactersInUsername_ShouldSucceed() throws Exception {
        // Arrange
        AuthDto.LoginRequest loginRequest = new AuthDto.LoginRequest("user@example.com", "password123");
        AuthDto.LoginResponse mockResponse = AuthDto.LoginResponse.builder()
                .token("token123")
                .username("user@example.com")
                .message("Login successful")
                .build();

        when(authService.login(any(AuthDto.LoginRequest.class))).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.username").value("user@example.com"));

        verify(authService, times(1)).login(any(AuthDto.LoginRequest.class));
    }

    @Test
    @DisplayName("POST /api/auth/register: should handle complex passwords")
    void register_WithComplexPassword_ShouldSucceed() throws Exception {
        // Arrange
        AuthDto.RegisterRequest registerRequest = new AuthDto.RegisterRequest(
                "testuser",
                "P@ss!W0rd#2024$Secure"
        );
        AuthDto.LoginResponse mockResponse = AuthDto.LoginResponse.builder()
                .token("token123")
                .username("testuser")
                .message("Registration successful")
                .build();

        when(authService.register(any(AuthDto.RegisterRequest.class))).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Registration successful"));

        verify(authService, times(1)).register(any(AuthDto.RegisterRequest.class));
    }

    @Test
    @DisplayName("POST /api/auth/login: should handle whitespace in credentials")
    void login_WithWhitespaceInCredentials_ShouldReturnBadRequest() throws Exception {
        // Arrange
        AuthDto.LoginRequest loginRequest = new AuthDto.LoginRequest("   ", "   ");

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).login(any());
    }

    @Test
    @DisplayName("POST /api/auth/login: should verify service was called exactly once")
    void login_ShouldCallServiceOnce() throws Exception {
        // Arrange
        AuthDto.LoginRequest loginRequest = new AuthDto.LoginRequest("user", "pass");
        AuthDto.LoginResponse mockResponse = AuthDto.LoginResponse.builder()
                .token("token")
                .username("user")
                .message("Login successful")
                .build();

        when(authService.login(any(AuthDto.LoginRequest.class))).thenReturn(mockResponse);

        // Act
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk());

        // Assert - verify service interaction
        verify(authService, times(1)).login(any(AuthDto.LoginRequest.class));
        verify(authService, never()).register(any());
    }

    @Test
    @DisplayName("POST /api/auth/register: should verify service was called exactly once")
    void register_ShouldCallServiceOnce() throws Exception {
        // Arrange
        AuthDto.RegisterRequest registerRequest = new AuthDto.RegisterRequest("newuser", "pass");
        AuthDto.LoginResponse mockResponse = AuthDto.LoginResponse.builder()
                .token("token")
                .username("newuser")
                .message("Registration successful")
                .build();

        when(authService.register(any(AuthDto.RegisterRequest.class))).thenReturn(mockResponse);

        // Act
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());

        // Assert - verify service interaction
        verify(authService, times(1)).register(any(AuthDto.RegisterRequest.class));
        verify(authService, never()).login(any());
    }
}

