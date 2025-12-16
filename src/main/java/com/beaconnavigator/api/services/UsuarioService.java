package com.beaconnavigator.api.services;

import com.beaconnavigator.api.models.Usuario;
import com.beaconnavigator.api.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
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

    public List<Usuario> listarTodos() {
        return repository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    public Usuario salvar(Usuario usuario) {
        if (usuario.getEmail() == null || usuario.getEmail().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email é obrigatório");
        }
        if (usuario.getSenha() == null || usuario.getSenha().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Senha é obrigatória");
        }

        if (repository.existsByEmail(usuario.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado");
        }

        // BCrypt aqui (salva criptografado)
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

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

        if (usuario.getSenha() != null && !usuario.getSenha().isBlank()) {
            existente.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }

        return repository.save(existente);
    }

    public Usuario atualizarParcial(Long id, Usuario usuarioParcial) {
        Usuario existente = buscarPorId(id);

        if (usuarioParcial.getEmail() != null && !usuarioParcial.getEmail().isBlank()) {
            validarEmailNaoUsadoPorOutro(usuarioParcial.getEmail(), id);
            existente.setEmail(usuarioParcial.getEmail());
        }

        if (usuarioParcial.getNomeCompleto() != null) {
            existente.setNomeCompleto(usuarioParcial.getNomeCompleto());
        }

        if (usuarioParcial.getSenha() != null && !usuarioParcial.getSenha().isBlank()) {
            existente.setSenha(passwordEncoder.encode(usuarioParcial.getSenha()));
        }

        return repository.save(existente);
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
