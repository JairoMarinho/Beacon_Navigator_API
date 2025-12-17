package com.beaconnavigator.api.controllers;

import java.util.List;
import java.util.stream.Collectors;

import com.beaconnavigator.api.models.Usuario;
import com.beaconnavigator.api.repository.TurmasMatriculasRepository;
import com.beaconnavigator.api.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.beaconnavigator.api.models.Turmas;
import com.beaconnavigator.api.services.TurmasService;
import com.beaconnavigator.api.models.TurmasMatriculas;

@RestController
@RequestMapping("/turmas")
@CrossOrigin(origins = "*")
public class TurmasController {

    @Autowired
    private TurmasService service;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TurmasMatriculasRepository matriculasRepository;

    // --- NOVO ENDPOINT: MINHAS TURMAS ---
    @GetMapping("/me")
    public ResponseEntity<List<Turmas>> minhasTurmas() {
        Usuario aluno = usuarioService.buscarUsuarioLogado();

        // Busca matrículas do aluno e extrai as turmas
        List<Turmas> minhas = matriculasRepository.findByUsuarioId(aluno.getId())
                .stream()
                .map(TurmasMatriculas::getTurma)
                .collect(Collectors.toList());

        return ResponseEntity.ok(minhas);
    }
    // ------------------------------------

    @GetMapping
    public ResponseEntity<List<Turmas>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Turmas> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

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