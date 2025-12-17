package com.beaconnavigator.api.services;

import com.beaconnavigator.api.dtos.UsuarioCreateRequest;
import com.beaconnavigator.api.models.Usuario;
import com.beaconnavigator.api.models.UsuarioPerfil;
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

    /**
     * MÉTODO DE CRIAÇÃO (Corrigido)
     * Recebe o DTO, cria o perfil com os nomes corretos e salva tudo.
     */
    public Usuario salvar(UsuarioCreateRequest dados) {
        // 1. Valida se o email já existe
        if (repository.existsByEmail(dados.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado");
        }

        // 2. Cria o Usuário (Dados de Login)
        Usuario usuario = new Usuario();
        usuario.setNomeCompleto(dados.getNomeCompleto());
        usuario.setEmail(dados.getEmail());
        usuario.setSenha(passwordEncoder.encode(dados.getSenha()));

        // 3. Cria o Perfil (Dados Extras)
        UsuarioPerfil perfil = new UsuarioPerfil();
        perfil.setBiografia(dados.getBiografia());
        perfil.setTelefone(dados.getTelefone());
        
        // --- CORREÇÃO DE MAPEAMENTO ---
        // O Front manda "cidade", mas sua tabela chama "localizacao"
        perfil.setLocalizacao(dados.getCidade()); 
        
        // O Front manda "estado", mas sua tabela chama "uf"
        perfil.setUf(dados.getEstado());
        
        // Define null ou uma imagem padrão se não vier nada
        perfil.setAvatarUrl(null); 

        // 4. Vincula o Perfil ao Usuário
        // Certifique-se que na classe Usuario o campo chama "userProfile" (ou ajuste para setPerfil)
        usuario.setUserProfile(perfil);

        // 5. Salva (CascadeType.ALL salvará o perfil automaticamente)
        return repository.save(usuario);
    }

    // --- Métodos de Atualização (Mantidos para compatibilidade) ---

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