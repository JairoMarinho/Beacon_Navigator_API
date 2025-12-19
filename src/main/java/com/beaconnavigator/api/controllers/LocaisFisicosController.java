package com.beaconnavigator.api.controllers;

import com.beaconnavigator.api.models.LocaisFisicos;
import com.beaconnavigator.api.services.LocaisFisicosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/locais")
public class LocaisFisicosController {

    @Autowired
    private LocaisFisicosService service;

    @GetMapping
    public ResponseEntity<List<LocaisFisicos>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocaisFisicos> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.buscarPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity<LocaisFisicos> criar(@RequestBody LocaisFisicos local) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(local));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody LocaisFisicos localAtualizado) {
        try {
            return ResponseEntity.ok(service.atualizar(id, localAtualizado));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            service.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}