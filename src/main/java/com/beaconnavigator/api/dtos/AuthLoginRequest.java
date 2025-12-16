package com.beaconnavigator.api.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AuthLoginRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String senha;

    public AuthLoginRequest() {}

    public AuthLoginRequest(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    public String getEmail() { return email; }
    public String getSenha() { return senha; }

    public void setEmail(String email) { this.email = email; }
    public void setSenha(String senha) { this.senha = senha; }
}
