package com.beaconnavigator.api.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDtos {

    public record RegisterRequest(
            @NotBlank @Email String email,
            @NotBlank @Size(min = 6, max = 72) String password,
            String name
    ) {}

    public record LoginRequest(
            @NotBlank @Email String email,
            @NotBlank String password
    ) {}

    // Ajuste os campos para o que o front espera (token, user, roles etc.)
    public record AuthResponse(
            String token,
            String tokenType,
            Object user
    ) {}
}
