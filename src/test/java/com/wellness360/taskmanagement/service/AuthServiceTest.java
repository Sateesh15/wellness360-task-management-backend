package com.wellness360.taskmanagement.service;

import com.wellness360.taskmanagement.dto.AuthDto;
import com.wellness360.taskmanagement.repository.UserRepository;
import com.wellness360.taskmanagement.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * WHY THIS TEST IS NEEDED:
     * This test verifies successful login flow.
     *
     * Login is one of the most critical features
     * in authentication systems.
     *
     * This test ensures:
     * 1. AuthenticationManager authenticates user
     * 2. JWT token is generated
     * 3. Correct response is returned
     *
     * Without this test, login functionality
     * may break silently.
     */
    @Test
    void shouldLoginSuccessfully() {

        AuthDto.LoginRequest request = new AuthDto.LoginRequest();
        request.setUsername("admin");
        request.setPassword("password123");

        when(authenticationManager.authenticate(any(
                UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(authentication.getName()).thenReturn("admin");

        when(jwtUtil.generateToken("admin"))
                .thenReturn("jwt-token");

        AuthDto.LoginResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("admin", response.getUsername());
        assertEquals("jwt-token", response.getToken());
        assertEquals("Login successful", response.getMessage());

        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        verify(jwtUtil, times(1))
                .generateToken("admin");
    }

    /**
     * WHY THIS TEST IS NEEDED:
     * This test verifies successful user registration.
     *
     * Registration is important because new users
     * must be stored securely with encoded passwords.
     *
     * This test ensures:
     * 1. Username uniqueness check happens
     * 2. Password gets encoded
     * 3. User is saved successfully
     * 4. JWT token is generated
     */
    @Test
    void shouldRegisterUserSuccessfully() {

        AuthDto.RegisterRequest request = new AuthDto.RegisterRequest();
        request.setUsername("newuser");
        request.setPassword("password123");

        when(userRepository.existsByUsername("newuser"))
                .thenReturn(false);

        when(passwordEncoder.encode("password123"))
                .thenReturn("encoded-password");

        when(jwtUtil.generateToken("newuser"))
                .thenReturn("jwt-token");

        AuthDto.LoginResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("newuser", response.getUsername());
        assertEquals("jwt-token", response.getToken());
        assertEquals("Registration successful", response.getMessage());

        verify(userRepository, times(1))
                .save("newuser", "encoded-password");

        verify(passwordEncoder, times(1))
                .encode("password123");

        verify(jwtUtil, times(1))
                .generateToken("newuser");
    }

    /**
     * WHY THIS TEST IS NEEDED:
     * This test verifies duplicate usernames
     * are rejected during registration.
     *
     * Allowing duplicate usernames could create
     * authentication conflicts and security issues.
     *
     * This test ensures proper validation exists.
     */
    @Test
    void shouldThrowExceptionWhenUsernameAlreadyExists() {

        AuthDto.RegisterRequest request = new AuthDto.RegisterRequest();
        request.setUsername("admin");
        request.setPassword("password123");

        when(userRepository.existsByUsername("admin"))
                .thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> authService.register(request)
        );

        assertEquals(
                "Username already exists: admin",
                exception.getMessage()
        );

        verify(userRepository, never())
                .save(anyString(), anyString());
    }

    /**
     * WHY THIS TEST IS NEEDED:
     * This test verifies password encoding
     * happens before saving users.
     *
     * Storing plain-text passwords is a major
     * security vulnerability.
     *
     * This test ensures passwords are securely encoded.
     */
    @Test
    void shouldEncodePasswordBeforeSavingUser() {

        AuthDto.RegisterRequest request = new AuthDto.RegisterRequest();
        request.setUsername("testuser");
        request.setPassword("mypassword");

        when(userRepository.existsByUsername("testuser"))
                .thenReturn(false);

        when(passwordEncoder.encode("mypassword"))
                .thenReturn("encoded-password");

        when(jwtUtil.generateToken("testuser"))
                .thenReturn("jwt-token");

        authService.register(request);

        verify(passwordEncoder, times(1))
                .encode("mypassword");

        verify(userRepository, times(1))
                .save("testuser", "encoded-password");
    }

    /**
     * WHY THIS TEST IS NEEDED:
     * This test verifies JWT token generation
     * after successful registration.
     *
     * Many applications automatically log in users
     * immediately after registration.
     *
     * This test ensures token generation works correctly.
     */
    @Test
    void shouldGenerateTokenAfterRegistration() {

        AuthDto.RegisterRequest request = new AuthDto.RegisterRequest();
        request.setUsername("demo");
        request.setPassword("password123");

        when(userRepository.existsByUsername("demo"))
                .thenReturn(false);

        when(passwordEncoder.encode(anyString()))
                .thenReturn("encoded-password");

        when(jwtUtil.generateToken("demo"))
                .thenReturn("generated-token");

        AuthDto.LoginResponse response =
                authService.register(request);

        assertEquals("generated-token", response.getToken());

        verify(jwtUtil, times(1))
                .generateToken("demo");
    }

    /**
     * WHY THIS TEST IS NEEDED:
     * This test verifies authentication manager
     * is called during login.
     *
     * AuthenticationManager is responsible for
     * validating username and password.
     *
     * Without calling authenticate(),
     * login security would completely fail.
     */
    @Test
    void shouldCallAuthenticationManagerDuringLogin() {

        AuthDto.LoginRequest request = new AuthDto.LoginRequest();
        request.setUsername("admin");
        request.setPassword("password123");

        when(authenticationManager.authenticate(any(
                UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(authentication.getName()).thenReturn("admin");

        when(jwtUtil.generateToken(anyString()))
                .thenReturn("jwt-token");

        authService.login(request);

        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}