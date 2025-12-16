package com.beaconnavigator.api.controllers;

import com.beaconnavigator.api.models.TurmasMatriculas;
import com.beaconnavigator.api.services.TurmasMatriculasService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/matriculas")
@CrossOrigin(origins = "*")
public class TurmasMatriculasController {

    @Autowired
    private TurmasMatriculasService service;

    // 1. LISTAR TODAS (Geral)
    @GetMapping
    public ResponseEntity<List<TurmasMatriculas>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    // 2. BUSCAR POR ID
    @GetMapping("/{id}")
    public ResponseEntity<TurmasMatriculas> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.buscarPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // 3. LISTAR POR TURMA (Ver quem está na sala)
    // Ex: GET /matriculas/turma/1
    @GetMapping("/turma/{turmaId}")
    public ResponseEntity<List<TurmasMatriculas>> listarPorTurma(@PathVariable Long turmaId) {
        return ResponseEntity.ok(service.listarPorTurma(turmaId));
    }

    // 4. LISTAR POR USUÁRIO (Ver minhas aulas)
    // Ex: GET /matriculas/usuario/5
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<TurmasMatriculas>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(service.listarPorUsuario(usuarioId));
    }

    // 5. REALIZAR MATRÍCULA (POST)
    @Operation(summary = "Realizar nova matrícula", description = "Vincula a matrícula a um aluno e uma turma existente")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Exemplo de payload com IDs simplificados", required = true, content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n"
            +
            "  \"usuario\": {\n" +
            "    \"id\": 2\n" +
            "  },\n" +
            "  \"turma\": {\n" +
            "    \"id\": 2\n" +
            "  },\n" +
            "  \"papel\": \"ESTUDANTE\",\n" +
            "  \"matricula\": \"0020015791\"\n" +
            "}")))
    @PostMapping
    public ResponseEntity<?> matricular(@RequestBody TurmasMatriculas matricula) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.realizarMatricula(matricula));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 6. CANCELAR MATRÍCULA (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelar(@PathVariable Long id) {
        try {
            service.cancelarMatricula(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}