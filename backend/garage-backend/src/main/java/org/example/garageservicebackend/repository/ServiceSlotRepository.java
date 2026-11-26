package org.example.garageservicebackend.repository;

import org.example.garageservicebackend.entity.ServiceSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.time.LocalTime;

public interface ServiceSlotRepository
        extends JpaRepository<ServiceSlot, UUID> {

    List<ServiceSlot> findByServiceTypeServiceTypeId(UUID serviceTypeId);

    List<ServiceSlot> findBySlotDateAndActiveTrue(LocalDate slotDate);

    List<ServiceSlot> findByServiceTypeServiceTypeIdAndSlotDateAndActiveTrue(
            UUID serviceTypeId,
            LocalDate slotDate
    );

    boolean existsBySlotDateAndActiveTrueAndStartTimeLessThanAndEndTimeGreaterThan(
            LocalDate slotDate,
            LocalTime endTime,
            LocalTime startTime
    );

    boolean existsBySlotDateAndActiveTrueAndStartTimeLessThanAndEndTimeGreaterThanAndSlotIdNot(
            LocalDate slotDate,
            LocalTime endTime,
            LocalTime startTime,
            UUID slotId
    );
}