package com.beaconnavigator.api.services;

import com.beaconnavigator.api.models.LocaisFisicos;
import com.beaconnavigator.api.repository.LocaisFisicosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocaisFisicosService {

    @Autowired
    private LocaisFisicosRepository repository;

    // 1. LISTAR TODOS
    public List<LocaisFisicos> listarTodos() {
        return repository.findAll();
    }

    // 2. BUSCAR POR ID
    public LocaisFisicos buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Local Físico não encontrado com ID: " + id));
    }

    // 3. SALVAR (Serve para Criar Novo e Atualizar)
    public LocaisFisicos salvar(LocaisFisicos local) {
        // if (local.getNome() == null || local.getNome().isEmpty()) ...
        return repository.save(local);
    }

    // 4. DELETAR
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Local não encontrado para exclusão.");
        }
        repository.deleteById(id);
    }
}