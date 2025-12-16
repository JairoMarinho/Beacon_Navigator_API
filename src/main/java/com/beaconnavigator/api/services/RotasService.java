package com.beaconnavigator.api.services;

import com.beaconnavigator.api.models.Rotas;
import com.beaconnavigator.api.models.Usuario;
import com.beaconnavigator.api.repository.RotasRepository;
import com.beaconnavigator.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RotasService {

    @Autowired
    private RotasRepository rotasRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Listar todas as rotas
    public List<Rotas> listarTodas() {
        return rotasRepository.findAll();
    }

    // Busca uma rota por ID
    public Rotas buscarPorId(Long id) {
        return rotasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rota não encontrada com ID: " + id));
    }

    // Salvar (Criação)
    @Transactional
    public Rotas salvar(Rotas rota) {
        // 1. Define data de criação se não houver
        if (rota.getDataCriacao() == null) {
            rota.setDataCriacao(LocalDateTime.now());
        }

        // 2. Valida e vincula o Proprietário (se informado)
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
        if (dadosAtualizados.getProprietario() != null && dadosAtualizados.getProprietario().getId() != null) {
            Usuario proprietario = usuarioRepository.findById(dadosAtualizados.getProprietario().getId())
                    .orElseThrow(() -> new RuntimeException("Usuário proprietário não encontrado"));
            rotaExistente.setProprietario(proprietario);
        }

        // OBS: Não recomendamos atualizar a LISTA de pontos aqui dentro (usar PontosRotasService),
        // mas se quiser manter a lógica de limpar e adicionar, ela permanece válida aqui.

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

    // --- MÉTODOS OTIMIZADOS ---

    // Lista apenas rotas públicas (Direto do banco, sem stream)
    public List<Rotas> listarRotasPublicas() {
        return rotasRepository.findByPublicaTrue();
    }

    // Lista rotas de um usuário específico (Direto do banco)
    public List<Rotas> listarRotasPorUsuario(Long usuarioId) {
        // Verifica se o usuário existe antes de buscar
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new RuntimeException("Usuário não encontrado");
        }
        return rotasRepository.findByProprietarioId(usuarioId);
    }

    // Alterna a visibilidade (Atalho útil para o Front-end)
    @Transactional
    public Rotas alternarVisibilidade(Long id) {
        Rotas rota = buscarPorId(id);
        rota.setPublica(!rota.isPublica());
        return rotasRepository.save(rota);
    }
}