package org.example.garageservicebackend.repository;

import org.example.garageservicebackend.entity.GarageManager;
import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.data.jpa.repository.EntityGraph;
import java.util.Optional;
import java.util.UUID;

public interface GarageManagerRepository
        extends JpaRepository<GarageManager, UUID> {

    @EntityGraph(attributePaths = {"personnel"})
    Optional<GarageManager> findByPersonnelPersonnelId(UUID personnelId);

}