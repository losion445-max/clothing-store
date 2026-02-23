package com.github.losion445_max.backend.infrastructure.security;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.github.losion445_max.backend.domain.user.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class JwtProviderImp implements JwtProvider {

    private final JwtProperties properties;
    
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
            properties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String generateToken(User user) {
        Instant now = Instant.now();
        Instant expireInstant = now.plusMillis(properties.getExpires());

        return Jwts.builder()
            .subject(user.getId().toString())
            .claim("role", user.getRole())
            .claim("username", user.getName())
            .issuedAt(Date.from(now))
            .expiration(Date.from(expireInstant))
            .signWith(getSigningKey())
            .compact();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException exc) {
            log.warn("Invalid JWT token: {}", exc.getMessage());
            return false;
        }
    }

    @Override
    public String getUsernameFromToken(String token) {
       Claims claims = parseClaims(token);
       String username = claims.get("username", String.class);
        if (username == null) {
            throw new IllegalArgumentException("username claim is missing");
        }

       return username;
    }

    @Override
    public String getRoleFromToken(String token) {
       Claims claims = parseClaims(token);
       String role = claims.get("role", String.class);
        if (role == null) {
            throw new IllegalArgumentException("Role claim is missing");
        }

       return role;
    }
    

    private Claims parseClaims(String token) {
        return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    public long getExpires() {
        return properties.getExpires();
    }

}
