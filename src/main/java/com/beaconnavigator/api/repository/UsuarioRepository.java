package com.beaconnavigator.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.beaconnavigator.api.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}
