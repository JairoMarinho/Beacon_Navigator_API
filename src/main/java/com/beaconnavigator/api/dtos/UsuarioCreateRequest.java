package com.beaconnavigator.api.dtos;

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

    // --- MUDANÇA AQUI: Campos soltos em vez do objeto UsuarioPerfil ---

    @JsonAlias({ "biografia", "bio" }) // Aceita "bio" do front ou "biografia"
    private String biografia;

    @JsonAlias("telefone")
    private String telefone;

    @JsonAlias({ "cidade", "location" }) // Aceita "location" do front
    private String cidade;

    @JsonAlias({ "estado", "state" }) // Aceita "state" do front
    private String estado;
}