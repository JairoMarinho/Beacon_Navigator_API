package com.beaconnavigator.api.dtos;

import com.beaconnavigator.api.models.UsuarioPerfil;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class UsuarioCreateRequest {

    @NotBlank
    @JsonAlias({ "nomeCompleto", "nome" })
    private String nomeCompleto;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @JsonAlias({ "senha", "password" })
    private String senha;

    @JsonAlias({ "confirmarSenha", "confirmPassword" })
    private String confirmarSenha;

    // NOVO CAMPO: Aceita o perfil do usuário
    @JsonAlias({ "userProfile", "perfil" })
    private UsuarioPerfil userProfile;
}