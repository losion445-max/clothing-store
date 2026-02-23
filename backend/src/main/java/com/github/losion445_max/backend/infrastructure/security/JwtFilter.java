package com.github.losion445_max.backend.infrastructure.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter{

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
            log.info("JwtFiler for  request={}", request.getPathInfo());
                

            String authHeader = request.getHeader("Authorization");
            
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.debug("Auth header is incorrect" + authHeader);
                filterChain.doFilter(request, response);
                return;
            }

            String token = authHeader.substring(7);

            try {
                if (jwtProvider.validateToken(token)) {
                    String id = jwtProvider.getIdFromToken(token);
                    String role = jwtProvider.getRoleFromToken(token);

                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);

                    UsernamePasswordAuthenticationToken authToken = 
                        new UsernamePasswordAuthenticationToken(id, null, List.of(authority));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.info("Successfully authenticated user with ID: {} for URI: {}", id, request.getRequestURI());
                    
                }
            
            } catch (Exception exc) {
                log.error("Authentication failed: {}", exc.getMessage());
            }

            filterChain.doFilter(request, response);
    }
    
}
