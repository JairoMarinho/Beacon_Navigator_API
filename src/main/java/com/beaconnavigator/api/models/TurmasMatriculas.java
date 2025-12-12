package com.beaconnavigator.api.models;

import java.time.LocalDateTime;

import com.beaconnavigator.api.constants.RoleMatricula;

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
@Entity(name = "tb_turmas_matricula")
public class TurmasMatriculas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false) // não pode ser atualizado depois da criação
    private LocalDateTime dataCriacao;

    // RELACIONAMENTO

    @ManyToOne
    @JoinColumn(name = "turma_id", nullable = false)
    private Turmas turma;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "role")
    private RoleMatricula papel; // Enum: ALUNO, PROFESSOR

}
