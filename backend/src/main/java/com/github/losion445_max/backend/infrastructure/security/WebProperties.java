package com.github.losion445_max.backend.infrastructure.security;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record WebProperties(
    List<String> allowedOrigins
) {}