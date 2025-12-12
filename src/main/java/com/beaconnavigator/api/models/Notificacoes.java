package com.beaconnavigator.api.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_notificacoes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notificacoes implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String titulo;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String mensagem;

    @Column(length = 50)
    private String tipo;

    @Column(name = "Foi_lida")
    private Boolean foiLida = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Beacon_origem_id")
    private Beacons beaconOrigem;

    @Column(name = "Data_envio")
    private LocalDateTime dataEnvio = LocalDateTime.now();
}