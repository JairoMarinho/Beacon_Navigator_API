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

    public List<Beacons> listarTodos() {
        return repository.findAll();
    }

    public Beacons buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Beacon não encontrado ID: " + id));
    }

    public Beacons salvar(Beacons beacon) {
        return repository.save(beacon);
    }

    // A MÁGICA DO PUT SEGURO ESTÁ AQUI:
    public Beacons atualizar(Long id, Beacons dadosNovos) {
        Beacons beaconExistente = buscarPorId(id);
        
        beaconExistente.setStatus(dadosNovos.getStatus());
        beaconExistente.setUltimaConexao(dadosNovos.getUltimaConexao());

        // Se mandou um local com ID, buscamos ele COMPLETO no banco
        // Isso impede que o nome da sala seja apagado
        if (dadosNovos.getLocal() != null && dadosNovos.getLocal().getId() != null) {
            LocaisFisicos localCompleto = locaisService.buscarPorId(dadosNovos.getLocal().getId());
            beaconExistente.setLocal(localCompleto);
        } else if (dadosNovos.getLocal() == null) {
            beaconExistente.setLocal(null);
        }

        return repository.save(beaconExistente);
    }

    public Beacons vincularLocal(Long beaconId, Long localId) {
        Beacons beacon = buscarPorId(beaconId);
        LocaisFisicos local = locaisService.buscarPorId(localId);
        beacon.setLocal(local);
        return repository.save(beacon);
    }

    public void deletar(Long id) {
        if (!repository.existsById(id)) throw new RuntimeException("Beacon não existe.");
        repository.deleteById(id);
    }
}