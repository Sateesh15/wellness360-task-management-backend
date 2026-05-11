package com.wellness360.taskmanagement.repository;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory user store for authentication.
 * Pre-seeded with a demo user for testing.
 */
@Repository
public class UserRepository {

    // Map of username -> BCrypt-hashed password
    // Pre-seeded: admin / password123
    private final Map<String, String> users = new ConcurrentHashMap<>(Map.of(
        "admin", "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy",
        "demo",  "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy"
    ));

    public Optional<String> findPasswordByUsername(String username) {
        return Optional.ofNullable(users.get(username));
    }

    public boolean existsByUsername(String username) {
        return users.containsKey(username);
    }

    public void save(String username, String hashedPassword) {
        users.put(username, hashedPassword);
    }
}
