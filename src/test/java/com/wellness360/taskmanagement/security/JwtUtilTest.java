package com.wellness360.taskmanagement.security;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {

        jwtUtil = new JwtUtil();

        // Setting private fields using ReflectionTestUtils
        // because @Value fields are not injected during unit tests
        ReflectionTestUtils.setField(
                jwtUtil,
                "secretKey",
                "wellness360-task-management-secret-key-2024-very-long-secret"
        );

        ReflectionTestUtils.setField(
                jwtUtil,
                "expirationMs",
                86400000L
        );
    }

    /**
     * WHY THIS TEST IS NEEDED:
     * This test verifies that JWT token generation works correctly.
     *
     * JWT generation is the core functionality of this utility.
     * If token creation fails, authentication cannot work.
     *
     * This test ensures:
     * 1. Token is generated successfully
     * 2. Generated token is not null
     * 3. Generated token is not empty
     */
    @Test
    void shouldGenerateJwtTokenSuccessfully() {

        String token = jwtUtil.generateToken("admin");

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    /**
     * WHY THIS TEST IS NEEDED:
     * This test verifies that username extraction works correctly.
     *
     * After login, applications usually extract username
     * from token for authentication and authorization.
     *
     * This test ensures token payload contains correct username.
     */
    @Test
    void shouldExtractUsernameFromToken() {

        String token = jwtUtil.generateToken("admin");

        String username = jwtUtil.extractUsername(token);

        assertEquals("admin", username);
    }

    /**
     * WHY THIS TEST IS NEEDED:
     * This test verifies that a valid token passes validation.
     *
     * Token validation is critical for application security.
     * Only valid tokens should allow access to secured APIs.
     *
     * This test ensures the validateToken() method
     * correctly identifies valid JWT tokens.
     */
    @Test
    void shouldValidateValidTokenSuccessfully() {

        String token = jwtUtil.generateToken("admin");

        boolean isValid = jwtUtil.validateToken(token);

        assertTrue(isValid);
    }

    /**
     * WHY THIS TEST IS NEEDED:
     * This test verifies invalid tokens are rejected.
     *
     * Security is extremely important in JWT authentication.
     * Fake or tampered tokens must never be accepted.
     *
     * This test ensures unauthorized access is prevented.
     */
    @Test
    void shouldReturnFalseForInvalidToken() {

        String invalidToken = "this.is.invalid.token";

        boolean isValid = jwtUtil.validateToken(invalidToken);

        assertFalse(isValid);
    }

    /**
     * WHY THIS TEST IS NEEDED:
     * This test verifies empty tokens are handled safely.
     *
     * Applications may accidentally receive empty tokens
     * from frontend or client requests.
     *
     * The method should safely return false instead of crashing.
     */
    @Test
    void shouldReturnFalseForEmptyToken() {

        boolean isValid = jwtUtil.validateToken("");

        assertFalse(isValid);
    }

    /**
     * WHY THIS TEST IS NEEDED:
     * This test verifies null token handling.
     *
     * Null values are common edge cases in APIs.
     * The application should handle them gracefully.
     *
     * This prevents NullPointerException issues.
     */
    @Test
    void shouldReturnFalseForNullToken() {

        boolean isValid = jwtUtil.validateToken(null);

        assertFalse(isValid);
    }

    /**
     * WHY THIS TEST IS NEEDED:
     * This test verifies tokens are user-specific.
     *
     * Each token should contain the correct user identity.
     * Wrong username extraction could lead to security issues.
     *
     * This ensures tokens belong to correct users.
     */
    @Test
    void shouldGenerateDifferentTokensForDifferentUsers() {

        String token1 = jwtUtil.generateToken("admin");
        String token2 = jwtUtil.generateToken("demo");

        String username1 = jwtUtil.extractUsername(token1);
        String username2 = jwtUtil.extractUsername(token2);

        assertEquals("admin", username1);
        assertEquals("demo", username2);
    }
}