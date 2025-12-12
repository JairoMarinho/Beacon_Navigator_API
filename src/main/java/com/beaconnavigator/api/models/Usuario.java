package com.beaconnavigator.api.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @Column(nullable = false, name = "NOME_COMPLETO", unique = true)
    private String nomeCompleto;

    @NotBlank(message = "Campo email é obrigatório")
    @Column(nullable = false, name = "EMAIL")
    @Email
    private String email;

    @NotBlank(message = "Campo senha obrigatório")
    @Column(name = "SENHA_HASH", nullable = false)
    @Size(min = 6, max = 25)
    private String senha; // NUNCA GUARDAR SENHA EM TEXTO PURO

    // Relacionamento Perfil (Cascade ALL)
    @OneToOne(mappedBy = "usuario_id", cascade = CascadeType.ALL, orphanRemoval = true)
    private UsuarioPerfil userProfile;
}
