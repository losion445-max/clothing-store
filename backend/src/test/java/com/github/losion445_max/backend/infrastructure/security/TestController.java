package com.github.losion445_max.backend.infrastructure.security;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
 
@RestController
public class TestController {
    @GetMapping("/api/test/public")
    public void publicEndpoint() {}

    @GetMapping("/api/test/protected")
    @PreAuthorize("hasRole('USER')")
    public void protectedEndpoint() {}
}