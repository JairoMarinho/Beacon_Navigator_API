package com.beaconnavigator.api.services;

import com.beaconnavigator.api.models.Turmas;
import com.beaconnavigator.api.models.TurmasMatriculas;
import com.beaconnavigator.api.models.Usuario;
import com.beaconnavigator.api.repository.TurmasMatriculasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TurmasMatriculasService {

    @Autowired
    private TurmasMatriculasRepository repository;

    @Autowired
    private UsuarioService usuarioService; // Para validar se o aluno existe

    @Autowired
    private TurmasService turmasService; // Para validar se a turma existe

    public List<TurmasMatriculas> listarTodas() {
        return repository.findAll();
    }

    public TurmasMatriculas buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matrícula não encontrada ID: " + id));
    }

    // Listar alunos de uma turma (Ex: Quem está na turma de Java?)
    public List<TurmasMatriculas> listarPorTurma(Long turmaId) {
        return repository.findByTurmaId(turmaId);
    }

    // Listar turmas de um usuário (Ex: Quais aulas o Jairo tem?)
    public List<TurmasMatriculas> listarPorUsuario(Long usuarioId) {
        return repository.findByUsuarioId(usuarioId);
    }

    public TurmasMatriculas realizarMatricula(TurmasMatriculas novaMatricula) {
        // 1. Validar Usuário
        if (novaMatricula.getUsuario() == null || novaMatricula.getUsuario().getId() == null) {
            throw new RuntimeException("ID do Usuário é obrigatório para matrícula.");
        }
        Usuario aluno = usuarioService.buscarPorId(novaMatricula.getUsuario().getId());

        // 2. Validar Turma
        if (novaMatricula.getTurma() == null || novaMatricula.getTurma().getId() == null) {
            throw new RuntimeException("ID da Turma é obrigatório para matrícula.");
        }
        Turmas turma = turmasService.buscarPorId(novaMatricula.getTurma().getId());

        // 3. Verificar Duplicidade
        if (repository.existsByTurmaIdAndUsuarioId(turma.getId(), aluno.getId())) {
            throw new RuntimeException("Este usuário já está matriculado nesta turma.");
        }

        // 4. Montar Objeto e Salvar
        novaMatricula.setUsuario(aluno);
        novaMatricula.setTurma(turma);
        novaMatricula.setDataCriacao(LocalDateTime.now());

        return repository.save(novaMatricula);
    }

    public void cancelarMatricula(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Matrícula não encontrada.");
        }
        repository.deleteById(id);
    }
}