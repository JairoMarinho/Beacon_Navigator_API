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
public class BearerTokenFilter extends OncePerRequestFilter {

    private static final String PREFIX = "Bearer ";

    /**
     * Token “fixo” para destravar o fluxo.
     * Quando você implementar JWT real, você substitui esta verificação.
     */
    private static final String VALID_TOKEN = "SEU_TOKEN_AQUI";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header != null && header.startsWith(PREFIX)) {
            String token = header.substring(PREFIX.length()).trim();

            if (VALID_TOKEN.equals(token)) {
                var auth = new UsernamePasswordAuthenticationToken(
                        "user",            // principal
                        null,              // credentials
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

        // Não filtra rotas públicas
        if ("/auth/login".equals(path)) return true;
        if ("/usuarios/teste".equals(path)) return true;

        // Swagger
        if (path.startsWith("/swagger-ui")) return true;
        if (path.startsWith("/v3/api-docs")) return true;

        // CORS preflight
        return "OPTIONS".equalsIgnoreCase(request.getMethod());
    }
}
