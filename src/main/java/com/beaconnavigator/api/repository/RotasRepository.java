package com.beaconnavigator.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.beaconnavigator.api.models.Rotas;

@Repository
public interface RotasRepository extends JpaRepository<Rotas, Long> {

}
