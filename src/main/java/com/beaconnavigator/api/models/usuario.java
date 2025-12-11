package com.beaconnavigator.api.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class usuario {

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
    @Size(min = 6, max = 25)
    private String senha; // NUNCA GUARDAR SENHA EM TEXTO PURO

}
