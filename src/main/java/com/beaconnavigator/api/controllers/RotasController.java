package com.beaconnavigator.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.beaconnavigator.api.models.Rotas;
import com.beaconnavigator.api.services.RotasService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;

@RestController
@RequestMapping("/rotas")
@CrossOrigin(origins = "*")
public class RotasController {

    @Autowired
    private RotasService service;

    // GET /rotas - Lista todas as rotas
    @GetMapping
    public ResponseEntity<List<Rotas>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    // GET /rotas/{id} - Busca uma rota por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.buscarPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // POST /rotas - Cria uma nova rota
    // POST - CRIAR ROTA
    @Operation(summary = "Criar uma nova rota", description = "Cria o cabeçalho da rota vinculada a um usuário")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "JSON simplificado para criação de rota", required = true, content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
            {
              "proprietario": {
                "id": 1
              },
              "nome": "Caminho da Biblioteca",
              "descricao": "Rota acess\u00edvel pelo elevador central",
              "publica": true
            }""")))
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Rotas rota) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(rota));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // PUT /rotas/{id} - Atualiza uma rota existente
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Rotas rota) {
        try {
            Rotas rotaAtualizada = service.atualizar(id, rota);
            return ResponseEntity.ok(rotaAtualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // DELETE /rotas/{id} - Deleta uma rota
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            service.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // GET /rotas/publicas - Lista apenas rotas públicas
    @GetMapping("/publicas")
    public ResponseEntity<List<Rotas>> listarPublicas() {
        List<Rotas> rotasPublicas = service.listarRotasPublicas();
        return ResponseEntity.ok(rotasPublicas);
    }

    // GET /rotas/usuario/{usuarioId} - Lista rotas de um usuário específico
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> listarPorUsuario(@PathVariable Long usuarioId) {
        try {
            List<Rotas> rotas = service.listarRotasPorUsuario(usuarioId);
            return ResponseEntity.ok(rotas);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // PATCH /rotas/{id}/visibilidade - Alterna entre pública/privada
    @PatchMapping("/{id}/visibilidade")
    public ResponseEntity<?> alternarVisibilidade(@PathVariable Long id) {
        try {
            Rotas rotaAtualizada = service.alternarVisibilidade(id);
            return ResponseEntity.ok(rotaAtualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // GET /rotas/teste - Rota de teste
    @GetMapping("/teste")
    public String testarApi() {
        return "🗺️ API de Rotas do Beacon Navigator está RODANDO com sucesso!";
    }
}