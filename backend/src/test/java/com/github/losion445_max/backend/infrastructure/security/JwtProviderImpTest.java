package com.github.losion445_max.backend.infrastructure.security;

import com.github.losion445_max.backend.domain.user.model.User;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
@DisplayName("JWT Provider Integration Tests")
class JwtProviderImpTest {

    private JwtProviderImp jwtProvider;
    private final String secret = "super-secret-key-that-is-at-least-32-chars-long-for-test";
    private final long expires = 3600000;

    @BeforeEach
    void setUp() {
        JwtProperties properties = new JwtProperties(secret, expires);
        jwtProvider = new JwtProviderImp(properties);
    }

    @Test
    @DisplayName("Successfully validate a properly signed and fresh token")
    void shouldValidateValidToken() {
        User user = createTestUser(UUID.randomUUID(), "Max");

        String token = jwtProvider.generateToken(user);

        assertNotNull(token);
        assertDoesNotThrow(() -> jwtProvider.validateToken(token));
    }

    @Test
    @DisplayName("Extract all required claims correctly from a valid token")
    void shouldExtractClaimsCorrectly() {
        UUID id = UUID.randomUUID();
        User user = createTestUser(id, "Max");

        String token = jwtProvider.generateToken(user);

        assertAll("Claims validation",
            () -> assertEquals(id.toString(), jwtProvider.getIdFromToken(token), "ID mismatch"),
            () -> assertEquals("Max", jwtProvider.getUsernameFromToken(token), "Username mismatch"),
            () -> assertEquals("USER", jwtProvider.getRoleFromToken(token), "Role mismatch")
        );
    }

    @Test
    @DisplayName("Throw SignatureException when token signature is tampered with")
    void shouldThrowSignatureExceptionOnTamperedToken() {
        User user = createTestUser(UUID.randomUUID(), "Hacker");
        String token = jwtProvider.generateToken(user);
        
        String tamperedToken = token.substring(0, token.length() - 1) + (token.endsWith("X") ? "Y" : "X");

        assertThrows(SignatureException.class, () -> jwtProvider.validateToken(tamperedToken));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "not.a.token", "header.payload.signature.extra"})
    @DisplayName("Throw MalformedJwtException or IllegalArgumentException on malformed input")
    void shouldThrowExceptionOnMalformedToken(String invalidToken) {
        assertThrows(Exception.class, () -> jwtProvider.validateToken(invalidToken));
    }

    @Test
    @DisplayName("Throw Exception when mandatory claims are missing from the token")
    void shouldThrowExceptionWhenClaimsAreMissing() {
        User user = User.builder()
                .id(UUID.randomUUID())
                .name(null) 
                .build();

        String token = jwtProvider.generateToken(user);

        assertThrows(RuntimeException.class, () -> jwtProvider.getUsernameFromToken(token));
    }

    @Test
    @DisplayName("Provide configured expiration time correctly")
    void shouldReturnConfiguredExpires() {
        assertEquals(expires, jwtProvider.getExpires());
    }

    private User createTestUser(UUID id, String name) {
        return User.builder()
                .id(id)
                .name(name)
                .role(User.Role.USER)
                .build();
    }
}