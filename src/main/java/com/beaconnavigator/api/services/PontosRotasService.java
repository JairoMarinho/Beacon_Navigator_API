package com.beaconnavigator.api.services;

import com.beaconnavigator.api.models.Beacons;
import com.beaconnavigator.api.models.LocaisFisicos;
import com.beaconnavigator.api.models.PontosRotas;
import com.beaconnavigator.api.models.Rotas;
import com.beaconnavigator.api.repository.PontosRotasRepository;
import com.beaconnavigator.api.repository.RotasRepository; // Precisa do repo de rotas
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PontosRotasService {

    @Autowired
    private PontosRotasRepository repository;

    @Autowired
    private RotasRepository rotasRepository; // Para validar a rota

    @Autowired
    private LocaisFisicosService locaisService; // Para validar o local

    @Autowired
    private BeaconService beaconService; // Para validar o beacon

    // 1. Listar todos (Geral)
    public List<PontosRotas> listarTodos() {
        return repository.findAll();
    }

    // 2. Buscar por ID
    public PontosRotas buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ponto de rota não encontrado ID: " + id));
    }

    // 3. Listar pontos de uma Rota Específica (Ordenados: Passo 1, 2, 3...)
    public List<PontosRotas> listarPorRota(Long rotaId) {
        return repository.findByRotaIdOrderBySequenceNumberAsc(rotaId);
    }

    // 4. SALVAR (Criação inteligente)
    public PontosRotas salvar(PontosRotas ponto) {

        // A. Validar e Vincular a ROTA (Obrigatório)
        if (ponto.getRota() != null && ponto.getRota().getId() != null) {
            Rotas rota = rotasRepository.findById(ponto.getRota().getId())
                    .orElseThrow(() -> new RuntimeException("Rota não encontrada!"));
            ponto.setRota(rota);
        } else {
            throw new RuntimeException("É obrigatório informar o ID da Rota para criar um ponto.");
        }

        // B. Validar e Vincular LOCAL FÍSICO
        if (ponto.getLocal() != null && ponto.getLocal().getId() != null) {
            LocaisFisicos local = locaisService.buscarPorId(ponto.getLocal().getId());
            ponto.setLocal(local);
        }

        // C. Validar e Vincular BEACON
        if (ponto.getBeacon() != null && ponto.getBeacon().getId() != null) {
            Beacons beacon = beaconService.buscarPorId(ponto.getBeacon().getId());
            ponto.setBeacon(beacon);
        }

        return repository.save(ponto);
    }

    // 5. ATUALIZAR (Para corrigir instrução ou ordem)
    public PontosRotas atualizar(Long id, PontosRotas dadosNovos) {
        PontosRotas pontoExistente = buscarPorId(id);

        // Atualiza dados simples
        pontoExistente.setInstruction(dadosNovos.getInstruction());
        pontoExistente.setSequenceNumber(dadosNovos.getSequenceNumber());

        // Se quiser mudar o Local vinculado (Ex: mudou de Lab 1 para Lab 2)
        if (dadosNovos.getLocal() != null && dadosNovos.getLocal().getId() != null) {
            LocaisFisicos novoLocal = locaisService.buscarPorId(dadosNovos.getLocal().getId());
            pontoExistente.setLocal(novoLocal);
        } else if (dadosNovos.getLocal() == null) {
            // Se enviou null, remove o vínculo
            pontoExistente.setLocal(null);
        }

        // Se quiser mudar o Beacon vinculado
        if (dadosNovos.getBeacon() != null && dadosNovos.getBeacon().getId() != null) {
            Beacons novoBeacon = beaconService.buscarPorId(dadosNovos.getBeacon().getId());
            pontoExistente.setBeacon(novoBeacon);
        } else if (dadosNovos.getBeacon() == null) {
            pontoExistente.setBeacon(null);
        }

        return repository.save(pontoExistente);
    }

    // 6. DELETAR
    public void deletar(Long id) {
        if (!repository.existsById(id))
            throw new RuntimeException("Ponto não existe.");
        repository.deleteById(id);
    }
}