package com.github.losion445_max.backend.infrastructure.security;

import com.github.losion445_max.backend.application.auth.AuthService;
import com.github.losion445_max.backend.web.auth.AuthController;
import com.github.losion445_max.backend.web.auth.dto.AuthMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@Tag("integration")
@WebMvcTest({AuthController.class, com.github.losion445_max.backend.infrastructure.security.TestController.class})
@Import(SecurityConfig.class)
class JwtFilterIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtProvider jwtProvider;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private AuthMapper authMapper;

    @Test
    @DisplayName("Should authenticate when token is valid")
    void doFilterInternal_ValidToken() throws Exception {
        String token = "valid.token.string";
        String id = "user-uuid";
        String role = "USER";

        when(jwtProvider.validateToken(token)).thenReturn(true);
        when(jwtProvider.getIdFromToken(token)).thenReturn(id);
        when(jwtProvider.getRoleFromToken(token)).thenReturn(role);

        mockMvc.perform(get("/api/test/protected")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        verify(jwtProvider).validateToken(token);
        verify(jwtProvider).getIdFromToken(token);
        verify(jwtProvider).getRoleFromToken(token);
    }

    @Test
    @DisplayName("Should not authenticate when token is invalid")
    void doFilterInternal_InvalidToken() throws Exception {
        String token = "invalid.token";

        when(jwtProvider.validateToken(token)).thenReturn(false);

        mockMvc.perform(get("/api/test/protected")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());

        verify(jwtProvider).validateToken(token);
    }

    @Test
    @DisplayName("Should skip filtering when Authorization header is missing")
    void doFilterInternal_NoHeader() throws Exception {
        mockMvc.perform(get("/api/test/protected"))
                .andExpect(status().isForbidden());

        verifyNoInteractions(jwtProvider);
    }

    @Test
    @DisplayName("Should catch and log exception when provider fails")
    void doFilterInternal_ExceptionInProvider() throws Exception {
        String token = "token.causing.exception";

        when(jwtProvider.validateToken(token)).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/api/test/protected")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());

        verify(jwtProvider).validateToken(token);
    }
}