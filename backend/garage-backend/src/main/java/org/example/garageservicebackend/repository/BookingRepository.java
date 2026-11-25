package org.example.garageservicebackend.repository;

import org.example.garageservicebackend.entity.Booking;
import org.example.garageservicebackend.entity.Booking.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BookingRepository
        extends JpaRepository<Booking, UUID> {

    List<Booking> findBySlotSlotId(UUID slotId);

    List<Booking> findByBookingStatus(BookingStatus bookingStatus);

    List<Booking> findByCustomerId(UUID customerId);

    boolean existsBySlotSlotIdAndCustomerId(UUID slotId, UUID customerId);
}