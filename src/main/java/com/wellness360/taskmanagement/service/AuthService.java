package com.wellness360.taskmanagement.service;

import com.wellness360.taskmanagement.dto.AuthDto;
import com.wellness360.taskmanagement.repository.UserRepository;
import com.wellness360.taskmanagement.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Authentication service for login and registration.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Authenticate user and return JWT token.
     */
    public AuthDto.LoginResponse login(AuthDto.LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        String token = jwtUtil.generateToken(authentication.getName());
        return AuthDto.LoginResponse.builder()
                .token(token)
                .username(authentication.getName())
                .message("Login successful")
                .build();
    }

    /**
     * Register a new user.
     */
    public AuthDto.LoginResponse register(AuthDto.RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + request.getUsername());
        }
        userRepository.save(request.getUsername(), passwordEncoder.encode(request.getPassword()));
        String token = jwtUtil.generateToken(request.getUsername());
        return AuthDto.LoginResponse.builder()
                .token(token)
                .username(request.getUsername())
                .message("Registration successful")
                .build();
    }
}
