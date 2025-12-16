package com.beaconnavigator.api.models;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.beaconnavigator.api.constants.StatusBeacon;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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
@Entity(name = "tb_beacons")
public class Beacons implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusBeacon status;

    @Column(name = "ULTIMA_CONEXAO")
    private LocalDateTime ultimaConexao;

    // RELACIONAMENTO
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "local_beacon_id", nullable = false)
    @JsonManagedReference
    private LocaisFisicos local;
}