package com.beaconnavigator.api.models;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.beaconnavigator.api.constants.StatusBeacon;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tb_beacons")
public class Beacons implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "local_beacon_id", nullable = false, unique = true)
    private LocaisFisicos local;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusBeacon status;

    @Column(name = "ULTIMA_CONEXAO")
    private LocalDateTime ultimaConexao;
}