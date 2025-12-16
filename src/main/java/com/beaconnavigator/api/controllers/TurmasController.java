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

import com.beaconnavigator.api.models.Turmas;
import com.beaconnavigator.api.services.TurmasService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;

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

    @Operation(summary = "Criar nova turma", description = "Cadastra uma turma vinculada a uma sala (local físico)")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "JSON simplificado para criação de turma", required = true, content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
            {
              "nomeTurma": "ADS - 4\u00ba Per\u00edodo",
              "descricao": "Turma de Desenvolvimento Back-end",
              "predio": "Faculdade Senac",
              "andar": "14",
              "sala": "1405",
              "textoHorario": "2025-03-10T08:00:00",
              "localFisico": {
                "id": 1
              }
            }""")))
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