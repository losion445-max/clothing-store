package com.github.losion445_max.backend.infrastructure.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private final String secret;
    private final long expires;
}
