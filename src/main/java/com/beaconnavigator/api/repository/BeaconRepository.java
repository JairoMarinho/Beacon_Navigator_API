package com.beaconnavigator.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.beaconnavigator.api.models.Beacons;

@Repository
public interface BeaconRepository extends JpaRepository<Beacons, Long> {

}
