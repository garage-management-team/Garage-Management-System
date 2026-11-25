package org.example.garageservicebackend.repository;

import org.example.garageservicebackend.entity.GaragePersonnel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GaragePersonnelRepository
        extends JpaRepository<GaragePersonnel, UUID> {

    Optional<GaragePersonnel> findByEmail(String email);

    boolean existsByEmail(String email);
}