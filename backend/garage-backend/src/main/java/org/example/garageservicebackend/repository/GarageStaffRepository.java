package org.example.garageservicebackend.repository;

import org.example.garageservicebackend.entity.GarageStaff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GarageStaffRepository
        extends JpaRepository<GarageStaff, UUID> {

    Optional<GarageStaff> findByPersonnelPersonnelId(UUID personnelId);

}