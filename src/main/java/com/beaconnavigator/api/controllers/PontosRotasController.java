package com.beaconnavigator.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.beaconnavigator.api.models.PontosRotas;
import com.beaconnavigator.api.services.PontosRotasService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;

@RestController
@RequestMapping("/pontos-rotas")
@CrossOrigin(origins = "*")
public class PontosRotasController {

    @Autowired
    private PontosRotasService service;

    // 1. LISTAR TODOS (Geral)
    @GetMapping
    public ResponseEntity<List<PontosRotas>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    // 2. BUSCAR POR ID
    @GetMapping("/{id}")
    public ResponseEntity<PontosRotas> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.buscarPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/rota/{rotaId}")
    public ResponseEntity<List<PontosRotas>> listarPorRota(@PathVariable Long rotaId) {
        return ResponseEntity.ok(service.listarPorRota(rotaId));
    }

    // 4. CRIAR PONTO (POST)
    @Operation(summary = "Criar um novo ponto de rota", description = "Adiciona um passo de navegação a uma rota existente")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "JSON simplificado para criação de ponto", required = true, content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
            {
              "rota": {
                "id": 1
              },
              "sequenceNumber": 1,
              "instruction": "Siga em frente at\u00e9 o bebedouro",
              "local": {
                "id": 10
              },
              "beacon": {
                "id": 5
              }
            }""")))
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody PontosRotas ponto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(ponto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 5. ATUALIZAR PONTO
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody PontosRotas ponto) {
        try {
            return ResponseEntity.ok(service.atualizar(id, ponto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 6. DELETAR PONTO
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            service.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}