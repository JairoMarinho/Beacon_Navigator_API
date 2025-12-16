package com.beaconnavigator.api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String PREFIX = "Bearer ";
    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header != null && header.startsWith(PREFIX)) {
            String token = header.substring(PREFIX.length()).trim();

            if (jwtService.isTokenValid(token)) {
                String subject = jwtService.extractSubject(token); // email

                var auth = new UsernamePasswordAuthenticationToken(
                        subject,              // principal = email
                        null,
                        Collections.emptyList()
                );

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) return true;

        // Rotas públicas
        if ("/auth/login".equals(path)) return true;
        if ("/usuarios".equals(path) && "POST".equalsIgnoreCase(request.getMethod())) return true;
        if ("/usuarios/teste".equals(path)) return true;

        // Swagger
        if (path.startsWith("/swagger-ui")) return true;
        if (path.startsWith("/v3/api-docs")) return true;

        return false;
    }
}
