package com.beaconnavigator.api.services;

import com.beaconnavigator.api.models.Usuario;
import com.beaconnavigator.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    public List<Usuario> listarTodos() {
        return repository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));
    }

    @Transactional
    public Usuario salvar(Usuario usuario) {
        Optional<Usuario> existente = repository.findByEmail(usuario.getEmail());

        if (existente.isPresent()) {
            throw new RuntimeException("Já existe um usuário cadastrado com este e-mail.");
        }

        return repository.save(usuario);
    }

    @Transactional
    public Usuario atualizar(Long id, Usuario dadosAtualizados) {
        Usuario usuarioExistente = buscarPorId(id);

        // 1. Atualiza dados básicos
        usuarioExistente.setNomeCompleto(dadosAtualizados.getNomeCompleto());
        usuarioExistente.setEmail(dadosAtualizados.getEmail());

        // 2. Atualiza a SENHA (se foi enviada uma nova)
        if (dadosAtualizados.getSenha() != null && !dadosAtualizados.getSenha().isEmpty()) {
            usuarioExistente.setSenha(dadosAtualizados.getSenha());
        }

        // 3. Atualiza o PERFIL (se foi enviado)
        if (dadosAtualizados.getUserProfile() != null) {
            if (usuarioExistente.getUserProfile() == null) {
                // Se não tinha perfil antes, cria um novo
                usuarioExistente.setUserProfile(dadosAtualizados.getUserProfile());
            } else {
                // Se já tinha, atualiza os campos internos
                var perfilVelho = usuarioExistente.getUserProfile();
                var perfilNovo = dadosAtualizados.getUserProfile();

                perfilVelho.setBiografia(perfilNovo.getBiografia());
                perfilVelho.setTelefone(perfilNovo.getTelefone());
                perfilVelho.setUf(perfilNovo.getUf());
                perfilVelho.setLocalizacao(perfilNovo.getLocalizacao());
                perfilVelho.setAvatarUrl(perfilNovo.getAvatarUrl());
            }
        }

        return repository.save(usuarioExistente);
    }

    @Transactional
    public Usuario atualizarParcial(Long id, Usuario dadosParciais) {
        Usuario usuarioExistente = buscarPorId(id);

        // 1. Atualiza Nome (se enviado)
        if (dadosParciais.getNomeCompleto() != null && !dadosParciais.getNomeCompleto().isBlank()) {
            usuarioExistente.setNomeCompleto(dadosParciais.getNomeCompleto());
        }

        // 2. Atualiza E-mail (se enviado, com validação de duplicidade)
        if (dadosParciais.getEmail() != null && !dadosParciais.getEmail().isBlank()) {
            // Se o e-mail mudou, verifica se já existe outro com ele
            if (!usuarioExistente.getEmail().equals(dadosParciais.getEmail())) {
                Optional<Usuario> emailCheck = repository.findByEmail(dadosParciais.getEmail());
                if (emailCheck.isPresent()) {
                    throw new RuntimeException("Este e-mail já está em uso por outro usuário.");
                }
            }
            usuarioExistente.setEmail(dadosParciais.getEmail());
        }

        // 3. Atualiza Senha (se enviada)
        if (dadosParciais.getSenha() != null && !dadosParciais.getSenha().isBlank()) {
            usuarioExistente.setSenha(dadosParciais.getSenha());
        }

        // 4. Atualiza Perfil (Parcialmente)
        if (dadosParciais.getUserProfile() != null) {
            // Se o usuário ainda não tem perfil, cria um novo com os dados parciais
            if (usuarioExistente.getUserProfile() == null) {
                usuarioExistente.setUserProfile(dadosParciais.getUserProfile());
            } else {
                // Atualiza apenas os campos do perfil que foram enviados
                var perfilVelho = usuarioExistente.getUserProfile();
                var perfilNovo = dadosParciais.getUserProfile();

                if (perfilNovo.getBiografia() != null)
                    perfilVelho.setBiografia(perfilNovo.getBiografia());
                if (perfilNovo.getTelefone() != null)
                    perfilVelho.setTelefone(perfilNovo.getTelefone());
                if (perfilNovo.getUf() != null)
                    perfilVelho.setUf(perfilNovo.getUf());
                if (perfilNovo.getLocalizacao() != null)
                    perfilVelho.setLocalizacao(perfilNovo.getLocalizacao());
                if (perfilNovo.getAvatarUrl() != null)
                    perfilVelho.setAvatarUrl(perfilNovo.getAvatarUrl());
            }
        }

        return repository.save(usuarioExistente);
    }

    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado para exclusão.");
        }
        repository.deleteById(id);
    }
}