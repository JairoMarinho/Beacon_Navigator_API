package com.beaconnavigator.api.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UsuarioCreateRequest {

    @NotBlank
    @JsonAlias({"nomeCompleto", "nome"})
    private String nomeCompleto;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @JsonAlias({"senha", "password"})
    private String senha;

    @JsonAlias({"confirmarSenha", "confirmPassword"})
    private String confirmarSenha;

    public String getNomeCompleto() { return nomeCompleto; }
    public String getEmail() { return email; }
    public String getSenha() { return senha; }
    public String getConfirmarSenha() { return confirmarSenha; }

    public void setNomeCompleto(String nomeCompleto) { this.nomeCompleto = nomeCompleto; }
    public void setEmail(String email) { this.email = email; }
    public void setSenha(String senha) { this.senha = senha; }
    public void setConfirmarSenha(String confirmarSenha) { this.confirmarSenha = confirmarSenha; }
}
