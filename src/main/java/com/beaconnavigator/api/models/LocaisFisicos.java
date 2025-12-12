package com.beaconnavigator.api.models;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tb_locais_fisicos")
public class LocaisFisicos implements Serializable {
    private static final long serialVersionUID = 1L;

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

    //Relacionamentos

    @OneToOne(mappedBy = "local", cascade = CascadeType.ALL)
    private Beacons beacon;

    @OneToMany(mappedBy = "localFisico")
    private List<Turmas> turmas;
}