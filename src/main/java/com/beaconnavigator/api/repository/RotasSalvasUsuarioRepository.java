package com.beaconnavigator.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.beaconnavigator.api.models.RotasSalvasUsuario;

@Repository
public interface RotasSalvasUsuarioRepository extends JpaRepository<RotasSalvasUsuario, Long> {

}
