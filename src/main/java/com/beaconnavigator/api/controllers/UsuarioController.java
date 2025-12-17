package com.beaconnavigator.api.controllers;

import com.beaconnavigator.api.dtos.UsuarioCreateRequest;
import com.beaconnavigator.api.models.Usuario;
import com.beaconnavigator.api.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    // --- NOVOS ENDPOINTS PESSOAIS ---

    // Pegar MEU perfil
    @GetMapping("/me")
    public ResponseEntity<Usuario> meuPerfil() {
        return ResponseEntity.ok(service.buscarUsuarioLogado());
    }

    // Atualizar MEU perfil
    @PutMapping("/me")
    public ResponseEntity<Usuario> atualizarMeuPerfil(@RequestBody Usuario dadosAtualizados) {
        Usuario eu = service.buscarUsuarioLogado();
        // Chama o serviço de atualização passando o ID do usuário logado
        return ResponseEntity.ok(service.atualizar(eu.getId(), dadosAtualizados));
    }

    // --------------------------------

    @GetMapping
    public ResponseEntity<List<Usuario>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Usuario> criar(@RequestBody @Valid UsuarioCreateRequest dados) {
        Usuario novoUsuario = service.salvar(dados);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Usuario usuario) {
        try {
            return ResponseEntity.ok(service.atualizar(id, usuario));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}