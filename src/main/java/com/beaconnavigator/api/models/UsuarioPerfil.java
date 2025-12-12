package com.beaconnavigator.api.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tb_usuario_perfil")
public class UsuarioPerfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String biografia;

    @NotBlank(message = "Campo telefone é obrigatório")
    @Column(nullable = false)
    private String telefone;

    @NotBlank(message = "Campo UF é obrigatório")
    @Column(nullable = false, name = "UF")
    @Size(max = 2)
    private String uf;

    @Column(nullable = false)
    private String localizacao;

    @Column(name = "AVATAR_URL")
    private String avatarUrl;

    // RELACIONAMENTO

}
