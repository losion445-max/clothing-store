package com.github.losion445_max.backend.infrastructure.security;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
    String secret,
    long expires
) {}