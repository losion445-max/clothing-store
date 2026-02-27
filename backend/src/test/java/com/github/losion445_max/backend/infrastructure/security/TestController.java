package com.github.losion445_max.backend.infrastructure.security;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
 
@RestController
public class TestController {
        @GetMapping("/api/test/public")
        public void publicEndpoint() {}

        @GetMapping("/api/test/protected")
        public void protectedEndpoint() {}
} 
