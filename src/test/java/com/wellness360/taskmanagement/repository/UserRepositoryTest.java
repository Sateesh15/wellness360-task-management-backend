package com.wellness360.taskmanagement.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepository();
    }

    // ─── findPasswordByUsername Tests ─────────────────────────────────────────

    @Test
    void shouldReturnPasswordWhenUsernameExists() {

        Optional<String> password =
                userRepository.findPasswordByUsername("admin");

        assertTrue(password.isPresent());
        assertEquals(
                "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy",
                password.get()
        );
    }

    // ─── existsByUsername Tests ──────────────────────────────────────────────

    @Test
    void shouldReturnEmptyWhenUsernameDoesNotExist() {

        Optional<String> password =
                userRepository.findPasswordByUsername("unknown");

        assertFalse(password.isPresent());
    }

    // ─── save Tests ───────────────────────────────────────────────────────────
    @Test
    void shouldReturnTrueWhenUsernameExists() {

        boolean exists = userRepository.existsByUsername("admin");

        assertTrue(exists);
    }

    // ─── save Tests ───────────────────────────────────────────────────────────

    @Test
    void shouldReturnFalseWhenUsernameDoesNotExist() {

        boolean exists = userRepository.existsByUsername("testuser");

        assertFalse(exists);
    }

    // ─── save Tests ───────────────────────────────────────────────────────────

    @Test
    void shouldSaveNewUserSuccessfully() {

        String username = "newuser";
        String hashedPassword = "hashedPassword123";

        userRepository.save(username, hashedPassword);

        Optional<String> savedPassword =
                userRepository.findPasswordByUsername(username);

        assertTrue(savedPassword.isPresent());
        assertEquals(hashedPassword, savedPassword.get());
    }

    // ─── save Tests ───────────────────────────────────────────────────────────

    @Test
    void shouldUpdateExistingUserPassword() {

        String username = "admin";
        String newPassword = "updatedHashedPassword";

        userRepository.save(username, newPassword);

        Optional<String> updatedPassword =
                userRepository.findPasswordByUsername(username);

        assertTrue(updatedPassword.isPresent());
        assertEquals(newPassword, updatedPassword.get());
    }
}