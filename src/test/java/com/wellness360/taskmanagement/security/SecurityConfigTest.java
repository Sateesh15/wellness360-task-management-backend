package com.wellness360.taskmanagement.security;

import com.wellness360.taskmanagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SecurityConfigTest {

    @Mock
    private JwtAuthFilter jwtAuthFilter;

    @Mock
    private UserRepository userRepository;

    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        securityConfig = new SecurityConfig(jwtAuthFilter, userRepository);
    }

    /**
     * WHY THIS TEST IS NEEDED:
     * This test verifies that PasswordEncoder bean
     * is created successfully.
     *
     * Password encoding is critical for application security.
     * Passwords should never be stored in plain text.
     *
     * This ensures BCryptPasswordEncoder is configured properly.
     */
    @Test
    void shouldCreatePasswordEncoderBean() {

        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();

        assertNotNull(passwordEncoder);

        String encodedPassword = passwordEncoder.encode("password123");

        assertTrue(passwordEncoder.matches("password123", encodedPassword));
    }

    /**
     * WHY THIS TEST IS NEEDED:
     * This test verifies that UserDetailsService
     * correctly loads existing users.
     *
     * Authentication depends on loading users correctly.
     * If user loading fails, login functionality breaks.
     *
     * This ensures proper integration with UserRepository.
     */
    @Test
    void shouldLoadUserByUsernameSuccessfully() {

        when(userRepository.findPasswordByUsername("admin"))
                .thenReturn(java.util.Optional.of("encodedPassword"));

        UserDetailsService userDetailsService =
                securityConfig.userDetailsService();

        UserDetails userDetails =
                userDetailsService.loadUserByUsername("admin");

        assertNotNull(userDetails);
        assertEquals("admin", userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
    }

    /**
     * WHY THIS TEST IS NEEDED:
     * This test verifies exception handling when
     * user does not exist.
     *
     * Security systems must reject unknown users properly.
     * Returning null instead of exception could create bugs
     * or security vulnerabilities.
     *
     * This ensures UsernameNotFoundException is thrown correctly.
     */
    @Test
    void shouldThrowExceptionWhenUserNotFound() {

        when(userRepository.findPasswordByUsername("unknown"))
                .thenReturn(java.util.Optional.empty());

        UserDetailsService userDetailsService =
                securityConfig.userDetailsService();

        assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("unknown")
        );
    }

    /**
     * WHY THIS TEST IS NEEDED:
     * This test verifies AuthenticationProvider bean creation.
     *
     * AuthenticationProvider is responsible for validating
     * usernames and passwords during login.
     *
     * This ensures DaoAuthenticationProvider is configured properly.
     */
    @Test
    void shouldCreateAuthenticationProviderSuccessfully() {

        AuthenticationProvider provider =
                securityConfig.authenticationProvider();

        assertNotNull(provider);

        assertTrue(provider instanceof DaoAuthenticationProvider);
    }

    /**
     * WHY THIS TEST IS NEEDED:
     * This test verifies that CORS configuration
     * is properly initialized.
     *
     * Frontend applications like React or Vue.js
     * depend on CORS configuration for API communication.
     *
     * Incorrect CORS settings can block frontend requests.
     */
    @Test
    void shouldCreateCorsConfigurationSourceSuccessfully() {

        CorsConfigurationSource source =
                securityConfig.corsConfigurationSource();

        assertNotNull(source);
    }

    /**
     * WHY THIS TEST IS NEEDED:
     * This test verifies that BCrypt password encoding
     * generates different hash values for same password.
     *
     * BCrypt uses random salt internally for better security.
     * Same password should not produce identical hashes.
     *
     * This improves password protection against attacks.
     */
    @Test
    void shouldGenerateDifferentHashesForSamePassword() {

        PasswordEncoder encoder = securityConfig.passwordEncoder();

        String hash1 = encoder.encode("password123");
        String hash2 = encoder.encode("password123");

        assertNotEquals(hash1, hash2);

        assertTrue(encoder.matches("password123", hash1));
        assertTrue(encoder.matches("password123", hash2));
    }

    /**
     * WHY THIS TEST IS NEEDED:
     * This test verifies that loaded users
     * receive USER role properly.
     *
     * Roles are important for authorization and access control.
     * Missing roles may prevent access to secured APIs.
     *
     * This ensures role assignment works correctly.
     */
    @Test
    void shouldAssignUserRoleCorrectly() {

        when(userRepository.findPasswordByUsername("admin"))
                .thenReturn(java.util.Optional.of("encodedPassword"));

        UserDetailsService userDetailsService =
                securityConfig.userDetailsService();

        UserDetails userDetails =
                userDetailsService.loadUserByUsername("admin");

        assertTrue(
                userDetails.getAuthorities()
                        .stream()
                        .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"))
        );
    }
}