package com.beaconnavigator.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.beaconnavigator.api.models.PontosRotas;

@Repository
public interface PontosRotasRepository extends JpaRepository<PontosRotas, Long> {
    List<PontosRotas> findByRotaIdOrderBySequenceNumberAsc(Long rotaId);
}
