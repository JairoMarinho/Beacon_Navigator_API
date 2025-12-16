package com.beaconnavigator.api.security;

import com.beaconnavigator.api.models.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey key;
    private final long expirationMinutes;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration-minutes:120}") long expirationMinutes
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMinutes = expirationMinutes;
    }

    public String generateToken(Usuario usuario) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(expirationMinutes * 60);

        return Jwts.builder()
                .subject(usuario.getEmail())              // sub = email (identidade)
                .claim("uid", usuario.getId())            // id do usuário
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(key)                            // HS256 por padrão
                .compact();
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid(String token) {
        try {
            Claims c = parseClaims(token);
            return c.getExpiration() != null && c.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public String extractSubject(String token) {
        return parseClaims(token).getSubject();
    }
}
