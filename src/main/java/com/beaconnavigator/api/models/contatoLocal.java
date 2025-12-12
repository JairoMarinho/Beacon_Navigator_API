package com.beaconnavigator.api.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tb_contato_local")
public class ContatoLocal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TICO_CONTATO", nullable = false)
    private String tipoContato;

    @Column(name = "CONTATO", nullable = false)
    private String contato;

    // RELACIONAMENTO
    @ManyToOne
    @JoinColumn(name = "LOCAL_FÍSICO_ID", nullable = false)
    private LocaisFisicos localFisicos;
}
