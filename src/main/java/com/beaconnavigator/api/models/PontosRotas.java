package com.beaconnavigator.api.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

@Entity
@Table(name = "ponto_rotas", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "rota_id", "sequencia_numero" }) 
                                                                           
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PontosRotas implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Rota_id", nullable = false)
    private Rotas rota;

    @Column(name = "Sequencia_numero", nullable = false)
    private Integer sequenceNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Localizacao_id")
    private LocaisFisicos local;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Beacon_id")
    private Beacons beacon;

    @Column(columnDefinition = "TEXT")
    private String instruction;
}