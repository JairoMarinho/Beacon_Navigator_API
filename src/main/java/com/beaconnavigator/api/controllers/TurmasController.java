package com.beaconnavigator.api.controllers;

import com.beaconnavigator.api.models.Turmas;
import com.beaconnavigator.api.services.TurmasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/turmas")
@CrossOrigin(origins = "*")
public class TurmasController {

    @Autowired
    private TurmasService service;

    @GetMapping
    public ResponseEntity<List<Turmas>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Turmas> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // Ex: /turmas/sala/1 -> Traz todas as aulas do Laboratório 1
    @GetMapping("/sala/{localId}")
    public ResponseEntity<List<Turmas>> listarPorSala(@PathVariable Long localId) {
        return ResponseEntity.ok(service.listarPorLocal(localId));
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Turmas turma) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(turma));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Turmas turma) {
        try {
            return ResponseEntity.ok(service.atualizar(id, turma));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}