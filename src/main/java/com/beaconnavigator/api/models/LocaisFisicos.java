package com.beaconnavigator.api.models;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Size;
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
    @Size(max = 2)
    private String estado;

    // --- RELACIONAMENTOS ---

    @OneToMany(mappedBy = "local", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Beacons> beacons;

    @OneToMany(mappedBy = "localFisico")
    @JsonIgnore // <--- 3. RECOMENDADO TAMBÉM (Evita loop futuro com Turmas)
    private List<Turmas> turmas;
}