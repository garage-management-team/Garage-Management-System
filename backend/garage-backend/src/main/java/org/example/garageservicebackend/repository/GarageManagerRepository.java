package org.example.garageservicebackend.repository;

import org.example.garageservicebackend.entity.GarageManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GarageManagerRepository
        extends JpaRepository<GarageManager, UUID> {

    Optional<GarageManager> findByPersonnelPersonnelId(UUID personnelId);

}