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

    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado para exclusão.");
        }
        repository.deleteById(id);
    }
}