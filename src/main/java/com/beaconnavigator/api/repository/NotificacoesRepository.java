package com.beaconnavigator.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.beaconnavigator.api.models.Notificacoes;

public interface NotificacoesRepository extends JpaRepository<Notificacoes, Long> {

}
