package com.beaconnavigator.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.beaconnavigator.api.models.Beacons;

public interface BeaconRepository extends JpaRepository<Beacons, Long> {

}
