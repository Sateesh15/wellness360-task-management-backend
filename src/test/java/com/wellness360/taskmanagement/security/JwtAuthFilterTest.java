package com.wellness360.taskmanagement.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtAuthFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() throws Exception {
        SecurityContextHolder.clearContext();
        closeable.close();
    }

    /**
     * WHY THIS TEST IS NEEDED:
     * This test verifies the main successful authentication flow.
     * It ensures that:
     * 1. JWT token is extracted correctly
     * 2. Token validation works
     * 3. Username is extracted
     * 4. UserDetails are loaded
     * 5. Authentication is stored in SecurityContextHolder
     *
     * This is the most critical test because authentication is the
     * core responsibility of this filter.
     */
    @Test
    void shouldAuthenticateUserWhenValidTokenProvided()
            throws ServletException, IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        request.addHeader("Authorization", "Bearer valid-token");

        UserDetails userDetails = new User(
                "admin",
                "password",
                Collections.emptyList()
        );

        when(jwtUtil.validateToken("valid-token")).thenReturn(true);
        when(jwtUtil.extractUsername("valid-token")).thenReturn("admin");
        when(userDetailsService.loadUserByUsername("admin"))
                .thenReturn(userDetails);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());

        assertEquals(
                "admin",
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName()
        );

        verify(filterChain, times(1))
                .doFilter(request, response);
    }

    /**
     * WHY THIS TEST IS NEEDED:
     * This test ensures that authentication is NOT set when
     * the token is invalid.
     *
     * Security is very important here because invalid JWT tokens
     * should never authenticate users.
     *
     * This prevents unauthorized access.
     */
    @Test
    void shouldNotAuthenticateWhenTokenIsInvalid()
            throws ServletException, IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        request.addHeader("Authorization", "Bearer invalid-token");

        when(jwtUtil.validateToken("invalid-token"))
                .thenReturn(false);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());

        verify(filterChain, times(1))
                .doFilter(request, response);

        verify(userDetailsService, never())
                .loadUserByUsername(anyString());
    }

    /**
     * WHY THIS TEST IS NEEDED:
     * This test verifies behavior when Authorization header
     * is completely missing.
     *
     * The filter should skip authentication safely and continue
     * request processing without throwing errors.
     *
     * This is important because many public endpoints may not
     * contain JWT tokens.
     */
    @Test
    void shouldNotAuthenticateWhenAuthorizationHeaderMissing()
            throws ServletException, IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());

        verify(filterChain, times(1))
                .doFilter(request, response);

        verify(jwtUtil, never()).validateToken(anyString());
    }

    /**
     * WHY THIS TEST IS NEEDED:
     * This test verifies that only Bearer tokens are accepted.
     *
     * If header format is incorrect, authentication should fail.
     *
     * Example invalid formats:
     * - Basic abc123
     * - Token xyz
     * - RandomText
     *
     * This ensures JWT standards are followed correctly.
     */
    @Test
    void shouldNotAuthenticateWhenHeaderDoesNotStartWithBearer()
            throws ServletException, IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        request.addHeader("Authorization", "Basic abc123");

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());

        verify(filterChain, times(1))
                .doFilter(request, response);

        verify(jwtUtil, never()).validateToken(anyString());
    }

    /**
     * WHY THIS TEST IS NEEDED:
     * This test ensures the filter continues the request chain
     * even when authentication is not successful.
     *
     * Filters must NEVER block the filter chain accidentally,
     * otherwise the application may hang or fail.
     *
     * This test guarantees request flow continuity.
     */
    @Test
    void shouldAlwaysContinueFilterChain()
            throws ServletException, IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1))
                .doFilter(request, response);
    }
}