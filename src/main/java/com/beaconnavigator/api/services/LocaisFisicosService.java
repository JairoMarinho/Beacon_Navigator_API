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

    public List<LocaisFisicos> listarTodos() {
        return repository.findAll();
    }

    public LocaisFisicos buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Local não encontrado ID: " + id));
    }

    public LocaisFisicos salvar(LocaisFisicos local) {
        return repository.save(local);
    }

    
    public LocaisFisicos atualizar(Long id, LocaisFisicos dadosNovos) {
        LocaisFisicos localExistente = buscarPorId(id);
        
        localExistente.setNome(dadosNovos.getNome());
        localExistente.setDescricao(dadosNovos.getDescricao());
        localExistente.setEndereco(dadosNovos.getEndereco());
        localExistente.setImagemUrl(dadosNovos.getImagemUrl());
        localExistente.setCidade(dadosNovos.getCidade());
        localExistente.setEstado(dadosNovos.getEstado());

        return repository.save(localExistente);
    }

    public void deletar(Long id) {
        if (!repository.existsById(id)) throw new RuntimeException("Local não existe.");
        repository.deleteById(id);
    }
}