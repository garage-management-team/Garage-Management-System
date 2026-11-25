package org.example.garageservicebackend.repository;

import org.example.garageservicebackend.entity.ServiceSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ServiceSlotRepository
        extends JpaRepository<ServiceSlot, UUID> {

    List<ServiceSlot> findByServiceTypeServiceTypeId(UUID serviceTypeId);

    List<ServiceSlot> findBySlotDateAndActiveTrue(LocalDate slotDate);

    List<ServiceSlot> findByServiceTypeServiceTypeIdAndSlotDateAndActiveTrue(
            UUID serviceTypeId,
            LocalDate slotDate
    );
}