package com.wellness360.taskmanagement.controller;

import com.wellness360.taskmanagement.dto.AuthDto;
import com.wellness360.taskmanagement.dto.TaskDto;
import com.wellness360.taskmanagement.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for authentication (login & registration).
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Login and register endpoints")
public class AuthController {

    private final AuthService authService;

    /**
     * POST /api/auth/login
     */
    @PostMapping("/login")
    @Operation(summary = "Login and receive JWT token")
    public ResponseEntity<TaskDto.ApiResponse<AuthDto.LoginResponse>> login(
            @Valid @RequestBody AuthDto.LoginRequest request) {
        AuthDto.LoginResponse response = authService.login(request);
        return ResponseEntity.ok(TaskDto.ApiResponse.success("Login successful", response));
    }

    /**
     * POST /api/auth/register
     */
    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<TaskDto.ApiResponse<AuthDto.LoginResponse>> register(
            @Valid @RequestBody AuthDto.RegisterRequest request) {
        AuthDto.LoginResponse response = authService.register(request);
        return ResponseEntity.ok(TaskDto.ApiResponse.success("Registration successful", response));
    }
}
