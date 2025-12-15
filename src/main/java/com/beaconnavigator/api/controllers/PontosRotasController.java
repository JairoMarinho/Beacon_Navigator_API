package com.beaconnavigator.api.controllers;

import com.beaconnavigator.api.models.PontosRotas;
import com.beaconnavigator.api.services.PontosRotasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // 4. CRIAR PONTO
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody PontosRotas ponto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(ponto));
        } catch (Exception e) {
            // Retorna erro 400 se faltar a Rota ou ID inválido
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