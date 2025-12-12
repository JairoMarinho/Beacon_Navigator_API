package com.beaconnavigator.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.beaconnavigator.api.models.UsuarioBeacon;

public interface UsuarioBeaconRepository extends JpaRepository<UsuarioBeacon, Long> {

}
