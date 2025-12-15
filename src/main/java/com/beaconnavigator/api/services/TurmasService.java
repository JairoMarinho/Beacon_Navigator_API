package com.beaconnavigator.api.services;

import com.beaconnavigator.api.models.LocaisFisicos;
import com.beaconnavigator.api.models.Turmas;
import com.beaconnavigator.api.repository.TurmasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TurmasService {

    @Autowired
    private TurmasRepository repository;

    @Autowired
    private LocaisFisicosService locaisService; // Para validar a sala

    public List<Turmas> listarTodas() {
        return repository.findAll();
    }

    public Turmas buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turma não encontrada ID: " + id));
    }

    public List<Turmas> listarPorLocal(Long localId) {
        return repository.findByLocalFisicoId(localId);
    }

    // SALVAR (Cria nova turma e vincula à sala)
    public Turmas salvar(Turmas turma) {
        // Verifica se veio um Local Físico no JSON
        if (turma.getLocalFisico() != null && turma.getLocalFisico().getId() != null) {
            // Busca a sala completa no banco para garantir que existe
            LocaisFisicos sala = locaisService.buscarPorId(turma.getLocalFisico().getId());
            turma.setLocalFisico(sala);
        }
        return repository.save(turma);
    }

    public Turmas atualizar(Long id, Turmas dadosNovos) {
        Turmas turmaExistente = buscarPorId(id);

        // 1. Atualiza os dados básicos (Texto)
        turmaExistente.setNomeTurma(dadosNovos.getNomeTurma());
        turmaExistente.setDescricao(dadosNovos.getDescricao());
        turmaExistente.setTextoHorario(dadosNovos.getTextoHorario());
        
        // ADICIONE ISSO (Para poder corrigir erros de cadastro):
        turmaExistente.setPredio(dadosNovos.getPredio());
        turmaExistente.setAndar(dadosNovos.getAndar());
        turmaExistente.setSala(dadosNovos.getSala());

        // 2. Verifica se a turma mudou de Local Físico (Opcional, mas recomendado)
        if (dadosNovos.getLocalFisico() != null && dadosNovos.getLocalFisico().getId() != null) {
            // Se mandou um ID de local novo, a gente busca e atualiza
            LocaisFisicos novaSala = locaisService.buscarPorId(dadosNovos.getLocalFisico().getId());
            turmaExistente.setLocalFisico(novaSala);
        }

        return repository.save(turmaExistente);
    }

    public void deletar(Long id) {
        if (!repository.existsById(id)) throw new RuntimeException("Turma não existe.");
        repository.deleteById(id);
    }
}