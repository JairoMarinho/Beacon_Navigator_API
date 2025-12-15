package com.beaconnavigator.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.beaconnavigator.api.models.Turmas;

import java.util.List;

@Repository
public interface TurmasRepository extends JpaRepository<Turmas, Long> {
    List<Turmas> findByLocalFisicoId(Long localId);
}