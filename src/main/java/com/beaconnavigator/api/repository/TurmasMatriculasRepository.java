package com.beaconnavigator.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.beaconnavigator.api.models.TurmasMatriculas;

@Repository
public interface TurmasMatriculasRepository extends JpaRepository<TurmasMatriculas, Long> {

  // Para listar todos os alunos de uma turma específica
  List<TurmasMatriculas> findByTurmaId(Long turmaId);

  // Para listar todas as turmas de um aluno específico
  List<TurmasMatriculas> findByUsuarioId(Long usuarioId);

  // Para evitar duplicidade (Aluno já está na turma?)
  boolean existsByTurmaIdAndUsuarioId(Long turmaId, Long usuarioId);

}