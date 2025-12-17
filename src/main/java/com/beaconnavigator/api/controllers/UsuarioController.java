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

    // 1. LISTAR TODOS (GET)
    @GetMapping
    public ResponseEntity<List<Usuario>> listarTodos() {
        List<Usuario> lista = service.listarTodos();
        return ResponseEntity.ok(lista);
    }

    // 2. BUSCAR POR ID (GET)
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        try {
            Usuario usuario = service.buscarPorId(id);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // 3. CRIAR NOVO USUÁRIO (POST)
    // Front espera 409 quando e-mail já existe

    @PostMapping
    // Certifique-se de importar o UsuarioCreateRequest e o @Valid
    public ResponseEntity<Usuario> criar(@RequestBody @Valid UsuarioCreateRequest dados) {

        // Agora o Service cuida de tudo (criar perfil, criptografar senha, salvar)
        Usuario novoUsuario = service.salvar(dados);

        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }

    // 4. ATUALIZAR (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Usuario usuario) {
        try {
            Usuario atualizado = service.atualizar(id, usuario);
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException e) {
            HttpStatus status = inferStatusFromMessage(e.getMessage(), HttpStatus.NOT_FOUND);
            return ResponseEntity.status(status).body(e.getMessage());
        }
    }

    // 5. DELETAR (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            service.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // 6. ATUALIZAR PARCIAL (PATCH)
    @PatchMapping("/{id}")
    public ResponseEntity<?> atualizarParcial(@PathVariable Long id, @RequestBody Usuario usuario) {
        try {
            Usuario atualizado = service.atualizarParcial(id, usuario);
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException e) {
            // 404 (não achou), 409 (email duplicado), 400 (validação)
            HttpStatus status = inferStatusFromMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
            return ResponseEntity.status(status).body(e.getMessage());
        }
    }

    // ROTA DE TESTE
    @GetMapping("/teste")
    public ResponseEntity<String> testarApi() {
        return ResponseEntity.ok("API do Beacon Navigator está rodando com sucesso! Pode continuar.");
    }

    /**
     * Heurística simples para mapear RuntimeException -> HTTP status
     * (sem mexer no Service agora).
     *
     * Ideal: criar exceções específicas e um @ControllerAdvice.
     */
    private HttpStatus inferStatusFromMessage(String message, HttpStatus defaultStatus) {
        if (message == null)
            return defaultStatus;

        String msg = message.toLowerCase();

        // conflito: e-mail já cadastrado / duplicado
        if (msg.contains("email")
                && (msg.contains("já") || msg.contains("ja") || msg.contains("exist") || msg.contains("duplic"))) {
            return HttpStatus.CONFLICT; // 409
        }

        // não encontrado
        if (msg.contains("não encontrado") || msg.contains("nao encontrado") || msg.contains("not found")) {
            return HttpStatus.NOT_FOUND; // 404
        }

        return defaultStatus;
    }
}
