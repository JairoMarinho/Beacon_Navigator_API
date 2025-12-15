package com.beaconnavigator.api.models;

import com.beaconnavigator.api.constants.StatusBeacon;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Entity(name = "tb_usuario_beacon")
public class UsuarioBeacon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // RELACIONAMENTO
    @ManyToOne
    @JoinColumn(name = "USUARIO_ID", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "userProfile"})
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "BEACON_ID", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "local"})
    private Beacons beacon;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "STATUS_BEACON")
    private StatusBeacon statusBeacon;
}