package com.beaconnavigator.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.beaconnavigator.api.models.UsuarioBeacon;

@Repository
public interface UsuarioBeaconRepository extends JpaRepository<UsuarioBeacon, Long> {

}
