package com.github.losion445_max.backend.infrastructure.security;

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
        UUID id = UUID.randomUUID();
        String name = "Max";
        String role = "USER";

        String token = jwtProvider.generateToken(id, role, name);

        assertNotNull(token);
        assertDoesNotThrow(() -> jwtProvider.validateToken(token));
    }

    @Test
    @DisplayName("Extract all required claims correctly from a valid token")
    void shouldExtractClaimsCorrectly() {
        UUID id = UUID.randomUUID();
        String name = "Max";
        String role = "USER";

        String token = jwtProvider.generateToken(id, role, name);

        assertAll("Claims validation",
            () -> assertEquals(id.toString(), jwtProvider.getIdFromToken(token)),
            () -> assertEquals(name, jwtProvider.getUsernameFromToken(token)),
            () -> assertEquals(role, jwtProvider.getRoleFromToken(token))
        );
    }

    @Test
    @DisplayName("Throw SignatureException when token signature is tampered with")
    void shouldThrowSignatureExceptionOnTamperedToken() {
        String token = jwtProvider.generateToken(UUID.randomUUID(), "USER", "Hacker");
        
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
        String token = jwtProvider.generateToken(UUID.randomUUID(), "USER", null);

        assertThrows(RuntimeException.class, () -> jwtProvider.getUsernameFromToken(token));
    }

    @Test
    @DisplayName("Provide configured expiration time correctly")
    void shouldReturnConfiguredExpires() {
        assertEquals(expires, jwtProvider.getExpires());
    }
}