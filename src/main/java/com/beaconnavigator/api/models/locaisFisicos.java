package com.beaconnavigator.api.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tb_locais_fisicos")
public class LocaisFisicos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "NOME")
    private String nome;

    @Column(name = "DESCRICAO")
    private String descricao;

    @Column(name = "IMAGEM_URL")
    private String imagemUrl;

    @Column(name = "ENDEREÇO")
    private String endereco;

    @Column(name = "CIDADE")
    private String cidade;

    @Column(name = "UF")
    private String estado;

    // RELACIONAMENTO

}
