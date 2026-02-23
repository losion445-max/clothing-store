package com.github.losion445_max.backend.infrastructure.security;


import com.github.losion445_max.backend.domain.user.model.User;

public interface JwtProvider {
    String generateToken(User user);
    boolean validateToken(String token);
    String getUsernameFromToken(String token);
    String getRoleFromToken(String token);
    long getExpires();
    String getIdFromToken(String token);
}
