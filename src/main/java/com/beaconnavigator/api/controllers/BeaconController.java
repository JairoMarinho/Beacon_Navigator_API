package com.beaconnavigator.api.controllers;

import com.beaconnavigator.api.models.Beacons;
import com.beaconnavigator.api.services.BeaconService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/beacons")
@CrossOrigin(origins = "*")
public class BeaconController {

    @Autowired
    private BeaconService service;

    // 1. LISTAR TODOS (GET)
    @GetMapping
    public ResponseEntity<List<Beacons>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    // 2. BUSCAR POR ID (GET)
    @GetMapping("/{id}")
    public ResponseEntity<Beacons> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.buscarPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // 3. CRIAR (POST)
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Beacons beacon) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(beacon));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 4. ATUALIZAR (PUT) - Este era o que estava faltando!
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Beacons beaconAtualizado) {
        try {
            // Garante que o ID do objeto seja o mesmo da URL
            beaconAtualizado.setId(id);
            return ResponseEntity.ok(service.salvar(beaconAtualizado));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 5. VINCULAR LOCAL (PUT ESPECIAL)
    @PutMapping("/{beaconId}/vincular-local/{localId}")
    public ResponseEntity<?> vincularLocal(@PathVariable Long beaconId, @PathVariable Long localId) {
        try {
            Beacons beaconAtualizado = service.vincularLocal(beaconId, localId);
            return ResponseEntity.ok(beaconAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
    // 6. DELETAR (DELETE)
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