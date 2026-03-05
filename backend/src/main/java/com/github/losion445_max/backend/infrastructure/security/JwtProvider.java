package com.github.losion445_max.backend.infrastructure.security;


import java.util.UUID;


public interface JwtProvider {
    String generateToken(UUID userId, String username, String role);
    void validateToken(String token);
    String getUsernameFromToken(String token);
    String getRoleFromToken(String token);
    long getExpires();
    String getIdFromToken(String token);
}
