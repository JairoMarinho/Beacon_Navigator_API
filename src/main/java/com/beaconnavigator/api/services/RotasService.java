package com.beaconnavigator.api.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beaconnavigator.api.models.Rotas;
import com.beaconnavigator.api.models.Usuario;
import com.beaconnavigator.api.repository.RotasRepository;
import com.beaconnavigator.api.repository.UsuarioRepository;

@Service
public class RotasService {

    @Autowired
    private RotasRepository rotasRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Lista todas as rotas
    public List<Rotas> listarTodas() {
        return rotasRepository.findAll();
    }

    // Busca uma rota por ID
    public Rotas buscarPorId(Long id) {
        return rotasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rota não encontrada com ID: " + id));
    }

    // Cria uma nova rota
    @Transactional
    public Rotas criar(Rotas rota) {
        // Valida se o proprietário existe
        if (rota.getProprietario() != null && rota.getProprietario().getId() != null) {
            Usuario proprietario = usuarioRepository.findById(rota.getProprietario().getId())
                    .orElseThrow(() -> new RuntimeException("Usuário proprietário não encontrado"));
            rota.setProprietario(proprietario);
        }

        return rotasRepository.save(rota);
    }

    // Atualiza uma rota existente
    @Transactional
    public Rotas atualizar(Long id, Rotas dadosAtualizados) {
        Rotas rotaExistente = buscarPorId(id);

        // Atualiza os campos básicos
        rotaExistente.setNome(dadosAtualizados.getNome());
        rotaExistente.setDescricao(dadosAtualizados.getDescricao());
        rotaExistente.setPublica(dadosAtualizados.isPublica());

        // Atualiza o proprietário se fornecido
        if (dadosAtualizados.getProprietario() != null &&
                dadosAtualizados.getProprietario().getId() != null) {
            Usuario proprietario = usuarioRepository.findById(dadosAtualizados.getProprietario().getId())
                    .orElseThrow(() -> new RuntimeException("Usuário proprietário não encontrado"));
            rotaExistente.setProprietario(proprietario);
        }

        // Atualiza os pontos da rota se fornecidos
        if (dadosAtualizados.getPontos() != null) {
            // Remove os pontos antigos
            if (rotaExistente.getPontos() != null) {
                rotaExistente.getPontos().clear();
            }

            // Adiciona os novos pontos
            dadosAtualizados.getPontos().forEach(ponto -> {
                ponto.setRota(rotaExistente);
                rotaExistente.getPontos().add(ponto);
            });
        }

        return rotasRepository.save(rotaExistente);
    }

    // Deleta uma rota
    @Transactional
    public void deletar(Long id) {
        if (!rotasRepository.existsById(id)) {
            throw new RuntimeException("Rota não encontrada para exclusão.");
        }
        rotasRepository.deleteById(id);
    }

    // Lista apenas rotas públicas
    public List<Rotas> listarRotasPublicas() {
        return rotasRepository.findAll().stream()
                .filter(Rotas::isPublica)
                .toList();
    }

    // Lista rotas de um usuário específico
    public List<Rotas> listarRotasPorUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return rotasRepository.findAll().stream()
                .filter(rota -> rota.getProprietario() != null &&
                        rota.getProprietario().getId().equals(usuarioId))
                .toList();
    }

    // Alterna a visibilidade da rota (pública/privada)
    @Transactional
    public Rotas alternarVisibilidade(Long id) {
        Rotas rota = buscarPorId(id);
        rota.setPublica(!rota.isPublica());
        return rotasRepository.save(rota);
    }
}