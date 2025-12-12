package com.beaconnavigator.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.beaconnavigator.api.models.LocaisFisicos;

@Repository
public interface LocaisFisicosRepository extends JpaRepository<LocaisFisicos, Long> {

}
