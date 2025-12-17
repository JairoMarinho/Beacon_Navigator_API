package com.beaconnavigator.api.services;

import com.beaconnavigator.api.dtos.UsuarioCreateRequest;
import com.beaconnavigator.api.models.Usuario;
import com.beaconnavigator.api.models.UsuarioPerfil;
import com.beaconnavigator.api.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder; // Importante
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    // --- NOVO MÉTODO: Pega o usuário do Token JWT ---
    public Usuario buscarUsuarioLogado() {
        // O e-mail é extraído automaticamente do contexto de segurança do Spring Security
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return repository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário logado não encontrado"));
    }

    public List<Usuario> listarTodos() {
        return repository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    public Usuario salvar(UsuarioCreateRequest dados) {
        if (repository.existsByEmail(dados.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNomeCompleto(dados.getNomeCompleto());
        usuario.setEmail(dados.getEmail());
        usuario.setSenha(passwordEncoder.encode(dados.getSenha()));

        UsuarioPerfil perfil = new UsuarioPerfil();
        perfil.setBiografia(dados.getBiografia());
        perfil.setTelefone(dados.getTelefone());
        perfil.setLocalizacao(dados.getCidade());
        perfil.setUf(dados.getEstado());
        perfil.setAvatarUrl(null);

        usuario.setUserProfile(perfil);

        return repository.save(usuario);
    }

    public Usuario atualizar(Long id, Usuario usuario) {
        Usuario existente = buscarPorId(id);

        if (usuario.getEmail() != null && !usuario.getEmail().isBlank()) {
            validarEmailNaoUsadoPorOutro(usuario.getEmail(), id);
            existente.setEmail(usuario.getEmail());
        }

        if (usuario.getNomeCompleto() != null) {
            existente.setNomeCompleto(usuario.getNomeCompleto());
        }
        
        // Atualiza campos do perfil se existirem
        if (usuario.getUserProfile() != null) {
            if (existente.getUserProfile() == null) {
                existente.setUserProfile(new UsuarioPerfil());
            }
            if (usuario.getUserProfile().getBiografia() != null) existente.getUserProfile().setBiografia(usuario.getUserProfile().getBiografia());
            if (usuario.getUserProfile().getTelefone() != null) existente.getUserProfile().setTelefone(usuario.getUserProfile().getTelefone());
            if (usuario.getUserProfile().getLocalizacao() != null) existente.getUserProfile().setLocalizacao(usuario.getUserProfile().getLocalizacao());
            if (usuario.getUserProfile().getUf() != null) existente.getUserProfile().setUf(usuario.getUserProfile().getUf());
        }

        if (usuario.getSenha() != null && !usuario.getSenha().isBlank()) {
            existente.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }

        return repository.save(existente);
    }

    public Usuario atualizarParcial(Long id, Usuario usuarioParcial) {
        return atualizar(id, usuarioParcial); // Reutilizando logica simples
    }

    public void deletar(Long id) {
        Usuario existente = buscarPorId(id);
        repository.delete(existente);
    }

    private void validarEmailNaoUsadoPorOutro(String email, Long meuId) {
        repository.findByEmail(email).ifPresent(outro -> {
            if (!outro.getId().equals(meuId)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado");
            }
        });
    }
}