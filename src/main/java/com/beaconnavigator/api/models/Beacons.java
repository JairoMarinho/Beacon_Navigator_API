package com.beaconnavigator.api.models;

import java.time.LocalDateTime;

import com.beaconnavigator.api.constants.StatusBeacon;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tb_beacons")
public class Beacons {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "LOCAL_BEACON_ID")
    private LocaisFisicos local;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusBeacon status;

    @Column(name = "ULTIMA_CONEXAO")
    private LocalDateTime ultimaConexao;

}
