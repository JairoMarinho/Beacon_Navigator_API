package com.beaconnavigator.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tb_usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Campo nome completo é obrigatório")
    @Column(nullable = false, name = "NOME_COMPLETO")
    private String nomeCompleto;

    @NotBlank(message = "Campo email é obrigatório")
    @Email
    @Column(nullable = false, name = "EMAIL", unique = true)
    private String email;

    @NotBlank(message = "Campo senha obrigatório")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "SENHA_HASH", nullable = false, length = 255)
    private String senha;

    @Column(name = "AVATAR_URL", length = 500)
    private String avatarUrl;

    // Relacionamento Perfil (Cascade ALL)
    @OneToOne(cascade = CascadeType.ALL)
    private UsuarioPerfil userProfile;
}
