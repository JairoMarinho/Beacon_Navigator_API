package com.beaconnavigator.api.controllers;

import com.beaconnavigator.api.models.Beacons;
import com.beaconnavigator.api.models.Usuario;
import com.beaconnavigator.api.models.UsuarioBeacon;
import com.beaconnavigator.api.repository.UsuarioBeaconRepository;
import com.beaconnavigator.api.services.BeaconService;
import com.beaconnavigator.api.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/beacons")
@CrossOrigin(origins = "*")
public class BeaconController {

    @Autowired
    private BeaconService service;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioBeaconRepository usuarioBeaconRepository;

    // --- NOVO ENDPOINT: MEUS BEACONS ---
    @GetMapping("/me")
    public ResponseEntity<List<Beacons>> meusBeacons() {
        Usuario usuario = usuarioService.buscarUsuarioLogado();
        
        // Busca na tabela de relacionamento (UsuarioBeacon) e extrai a lista de Beacons
        List<Beacons> meusBeacons = usuarioBeaconRepository.findAll().stream()
                .filter(ub -> ub.getUsuario().getId().equals(usuario.getId()))
                .map(UsuarioBeacon::getBeacon)
                .collect(Collectors.toList());

        return ResponseEntity.ok(meusBeacons);
    }
    // -----------------------------------

    @GetMapping
    public ResponseEntity<List<Beacons>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Beacons> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Beacons beacon) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(beacon));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Beacons beaconAtualizado) {
        try {
            return ResponseEntity.ok(service.atualizar(id, beaconAtualizado));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{beaconId}/vincular-local/{localId}")
    public ResponseEntity<?> vincularLocal(@PathVariable Long beaconId, @PathVariable Long localId) {
        return ResponseEntity.ok(service.vincularLocal(beaconId, localId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}