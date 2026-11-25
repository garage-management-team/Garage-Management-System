package org.example.garageservicebackend.repository;

import org.example.garageservicebackend.entity.SlotStaffAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SlotStaffAssignmentRepository
        extends JpaRepository<SlotStaffAssignment, UUID> {

    List<SlotStaffAssignment> findBySlotSlotId(UUID slotId);

    List<SlotStaffAssignment> findByStaffStaffId(UUID staffId);

    boolean existsBySlotSlotIdAndStaffStaffId(UUID slotId, UUID staffId);
}