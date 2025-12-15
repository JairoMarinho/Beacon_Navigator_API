package com.beaconnavigator.api.services;

import com.beaconnavigator.api.models.Beacons;
import com.beaconnavigator.api.models.LocaisFisicos;
import com.beaconnavigator.api.repository.BeaconRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BeaconService {

    @Autowired
    private BeaconRepository repository;

    @Autowired
    private LocaisFisicosService locaisService;

    // 1. LISTAR TODOS
    public List<Beacons> listarTodos() {
        return repository.findAll();
    }

    // 2. BUSCAR POR ID
    public Beacons buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Beacon não encontrado com ID: " + id));
    }

    // 3. SALVAR (Novo ou Atualização)
    public Beacons salvar(Beacons beacon) {

        // Se o JSON trouxe um Local apenas com ID (sem nome, sem descrição...),
        // nós buscamos o local completo no banco para evitar que o Hibernate
        if (beacon.getLocal() != null && beacon.getLocal().getId() != null) {
            // Busca a sala completa no banco (com nome, endereço, etc)
            LocaisFisicos localCompleto = locaisService.buscarPorId(beacon.getLocal().getId());

            // Substitui o local "vazio" do JSON pelo local "cheio" do banco
            beacon.setLocal(localCompleto);
        }

        return repository.save(beacon);
    }

    // 4. VINCULAR BEACON A UM LOCAL (Método Especial)
    // Esse método é chamado pelo Controller quando fazemos PUT
    // /beacons/1/vincular-local/5
    public Beacons vincularLocal(Long beaconId, Long localId) {
        // Busca o Beacon (ou erro se não achar)
        Beacons beacon = buscarPorId(beaconId);

        // Busca o Local (ou erro se não achar)
        LocaisFisicos local = locaisService.buscarPorId(localId);

        // Faz a conexão
        beacon.setLocal(local);

        // Salva a alteração no banco
        return repository.save(beacon);
    }

    // 5. DELETAR
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Beacon não encontrado para exclusão.");
        }
        repository.deleteById(id);
    }
}