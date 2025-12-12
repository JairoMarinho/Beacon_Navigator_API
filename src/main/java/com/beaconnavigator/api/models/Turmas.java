package com.beaconnavigator.api.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tb_turmas")
public class Turmas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Campo nome turma é obrigatório")
    @Column(nullable = false)
    private String nomeTurma;

    @Column(nullable = false)
    private String descricao;

    @NotBlank(message = "Campo prédio é obrigatório")
    @Column(nullable = false)
    private String predio;

    @NotBlank(message = "Campo andar é obrigatório")
    @Column(nullable = false)
    private String andar;

    @Column(nullable = false)
    private String sala;

    @Column(nullable = false)
    private LocalDateTime textoHorario; // texto com horário: (Seg e Qua, 19h-22h)

    // RELACIONAMENTO

}
