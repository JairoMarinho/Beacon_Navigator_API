package com.beaconnavigator.api.services;

import com.beaconnavigator.api.dtos.UsuarioCreateRequest;
import com.beaconnavigator.api.dtos.UsuarioChangePasswordRequest;
import com.beaconnavigator.api.models.Usuario;
import com.beaconnavigator.api.models.UsuarioPerfil;
import com.beaconnavigator.api.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
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

    // --- Pega o usuário do Token JWT ---
    public Usuario buscarUsuarioLogado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return repository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário logado não encontrado"));
    }

    // --- NOVO: Alterar senha do usuário logado com segurança ---
    public void alterarSenhaUsuarioLogado(UsuarioChangePasswordRequest req) {
        if (req == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Requisição inválida");
        }

        String senhaAtual = req.getSenhaAtual();
        String novaSenha = req.getNovaSenha();
        String confirmacao = req.getConfirmacao();

        if (senhaAtual == null || senhaAtual.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Senha atual é obrigatória");
        }
        if (novaSenha == null || novaSenha.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nova senha é obrigatória");
        }
        if (confirmacao == null || confirmacao.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Confirmação é obrigatória");
        }
        if (!novaSenha.equals(confirmacao)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Confirmação da nova senha não confere");
        }
        if (novaSenha.length() < 8) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nova senha deve ter no mínimo 8 caracteres");
        }

        Usuario eu = buscarUsuarioLogado();

        // Valida senha atual (comparando hash)
        boolean senhaConfere = passwordEncoder.matches(senhaAtual, eu.getSenha());
        if (!senhaConfere) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Senha atual incorreta");
        }

        // Evita trocar para a mesma senha (opcional, mas recomendado)
        if (passwordEncoder.matches(novaSenha, eu.getSenha())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A nova senha não pode ser igual à senha atual");
        }

        eu.setSenha(passwordEncoder.encode(novaSenha));
        repository.save(eu);

        // Recomendado (opcional):
        // - invalidar tokens/sessões anteriores
        // (depende de como seu JWT está implementado)
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
            if (usuario.getUserProfile().getBiografia() != null)
                existente.getUserProfile().setBiografia(usuario.getUserProfile().getBiografia());
            if (usuario.getUserProfile().getTelefone() != null)
                existente.getUserProfile().setTelefone(usuario.getUserProfile().getTelefone());
            if (usuario.getUserProfile().getLocalizacao() != null)
                existente.getUserProfile().setLocalizacao(usuario.getUserProfile().getLocalizacao());
            if (usuario.getUserProfile().getUf() != null)
                existente.getUserProfile().setUf(usuario.getUserProfile().getUf());
        }

        // OBS: Por segurança, recomendo REMOVER a troca de senha daqui
        // e usar apenas /usuarios/me/senha.
        if (usuario.getSenha() != null && !usuario.getSenha().isBlank()) {
            existente.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }

        return repository.save(existente);
    }

    public Usuario atualizarParcial(Long id, Usuario usuarioParcial) {
        return atualizar(id, usuarioParcial);
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
