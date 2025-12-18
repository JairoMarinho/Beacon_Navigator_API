package com.beaconnavigator.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration c = new CorsConfiguration();

        // Troque pelo(s) domínio(s) do seu front (exemplos):
        // c.setAllowedOrigins(List.of("https://app.seudominio.com"));
        // Para dev local, você pode incluir:
        // c.setAllowedOrigins(List.of("http://localhost:3000"));

        c.setAllowedOrigins(List.of(
            "http://localhost:5173"
        ));

        c.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        c.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        c.setExposedHeaders(List.of("Location"));
        c.setAllowCredentials(false); // JWT Bearer não precisa de cookies; manter false é mais seguro.
        c.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", c);
        return source;
    }
}
