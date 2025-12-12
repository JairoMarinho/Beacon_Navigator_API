package com.beaconnavigator.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.beaconnavigator.api.models.ContatoLocal;

@Repository
public interface ContatoLocalRepository extends JpaRepository<ContatoLocal, Long> {

}
