package com.beaconnavigator.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.beaconnavigator.api.models.Rotas;
import com.beaconnavigator.api.services.RotasService;

@RestController
@RequestMapping("/rotas")
@CrossOrigin(origins = "*")
public class RotasController {

    @Autowired
    private RotasService service;

    /**
     * GET /rotas - Lista todas as rotas
     */
    @GetMapping
    public ResponseEntity<List<Rotas>> listarTodas() {
        List<Rotas> rotas = service.listarTodas();
        return ResponseEntity.ok(rotas);
    }

    /**
     * GET /rotas/{id} - Busca uma rota por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            Rotas rota = service.buscarPorId(id);
            return ResponseEntity.ok(rota);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    /**
     * POST /rotas - Cria uma nova rota
     */
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Rotas rota) {
        try {
            Rotas novaRota = service.criar(rota);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaRota);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    /**
     * PUT /rotas/{id} - Atualiza uma rota existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Rotas rota) {
        try {
            Rotas rotaAtualizada = service.atualizar(id, rota);
            return ResponseEntity.ok(rotaAtualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    /**
     * DELETE /rotas/{id} - Deleta uma rota
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            service.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    /**
     * GET /rotas/publicas - Lista apenas rotas públicas
     */
    @GetMapping("/publicas")
    public ResponseEntity<List<Rotas>> listarPublicas() {
        List<Rotas> rotasPublicas = service.listarRotasPublicas();
        return ResponseEntity.ok(rotasPublicas);
    }

    /**
     * GET /rotas/usuario/{usuarioId} - Lista rotas de um usuário específico
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> listarPorUsuario(@PathVariable Long usuarioId) {
        try {
            List<Rotas> rotas = service.listarRotasPorUsuario(usuarioId);
            return ResponseEntity.ok(rotas);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    /**
     * PATCH /rotas/{id}/visibilidade - Alterna entre pública/privada
     */
    @PatchMapping("/{id}/visibilidade")
    public ResponseEntity<?> alternarVisibilidade(@PathVariable Long id) {
        try {
            Rotas rotaAtualizada = service.alternarVisibilidade(id);
            return ResponseEntity.ok(rotaAtualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    /**
     * GET /rotas/teste - Rota de teste
     */
    @GetMapping("/teste")
    public String testarApi() {
        return "🗺️ API de Rotas do Beacon Navigator está RODANDO com sucesso!";
    }
}