package com.beaconnavigator.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.beaconnavigator.api.models.Rotas;
import java.util.List;

@Repository
public interface RotasRepository extends JpaRepository<Rotas, Long> {
    List<Rotas> findByPublicaTrue();

    // Busca otimizada: O SQL já traz apenas as do usuário X
    List<Rotas> findByProprietarioId(Long usuarioId);
}
