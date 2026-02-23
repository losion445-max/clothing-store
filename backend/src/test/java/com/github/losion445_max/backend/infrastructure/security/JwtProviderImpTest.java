package com.github.losion445_max.backend.infrastructure.security;

import com.github.losion445_max.backend.domain.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
class JwtProviderImpTest {

    private JwtProviderImp jwtProvider;
    private final String secret = "super-secret-key-at-least-32-characters-long-for-test";
    private final long expires = 3600000;

    @BeforeEach
    void setUp() {
        JwtProperties properties = new JwtProperties(secret, expires);
        jwtProvider = new JwtProviderImp(properties);
    }

    @Test
    @DisplayName("Should generate and validate token")
    void generateAndValidateToken() {
        User user = User.builder()
                .id(UUID.randomUUID())
                .name("Max")
                .role(User.Role.USER)
                .build();

        String token = jwtProvider.generateToken(user);

        assertNotNull(token);
        assertTrue(jwtProvider.validateToken(token));
    }

    @Test
    @DisplayName("Should extract correct data from token")
    void extractDataFromToken() {
        UUID id = UUID.randomUUID();
        User user = User.builder()
                .id(id)
                .name("Max")
                .role(User.Role.USER)
                .build();

        String token = jwtProvider.generateToken(user);

        assertEquals(id.toString(), jwtProvider.getIdFromToken(token));
        assertEquals("Max", jwtProvider.getUsernameFromToken(token));
        assertEquals("USER", jwtProvider.getRoleFromToken(token));
    }

    @Test
    @DisplayName("Should return false for invalid token")
    void validateInvalidToken() {
        assertFalse(jwtProvider.validateToken("invalid.token.string"));
        assertFalse(jwtProvider.validateToken(null));
    }

    @Test
    @DisplayName("Should throw exception when username claim is missing")
    void getUsername_MissingClaim() {
        User user = User.builder()
                .id(UUID.randomUUID())
                .name(null)
                .build();

        String token = jwtProvider.generateToken(user);

        assertThrows(IllegalArgumentException.class, () -> jwtProvider.getUsernameFromToken(token));
    }

    @Test
    @DisplayName("Should throw exception when role claim is missing")
    void getRole_MissingClaim() {
        User user = User.builder()
                .id(UUID.randomUUID())
                .name("Max")
                .role(null)
                .build();

        String token = jwtProvider.generateToken(user);

        assertThrows(IllegalArgumentException.class, () -> jwtProvider.getRoleFromToken(token));
    }

    @Test
    @DisplayName("Should return correct expires value")
    void getExpires() {
        assertEquals(expires, jwtProvider.getExpires());
    }
}