package com.beaconnavigator.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.beaconnavigator.api.models.Notificacoes;

@Repository
public interface NotificacoesRepository extends JpaRepository<Notificacoes, Long> {

}
