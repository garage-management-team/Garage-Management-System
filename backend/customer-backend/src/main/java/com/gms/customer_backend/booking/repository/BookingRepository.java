package com.gms.customer_backend.booking.repository;

import com.gms.customer_backend.booking.entity.Booking;
import com.gms.customer_backend.booking.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {

    // Get all bookings belonging to a customer
    List<Booking> findAllByCustomer_CustomerId(UUID customerId);

    // Find a particular booking belonging to a customer
    Optional<Booking> findByBookingIdAndCustomer_CustomerId(
            UUID bookingId,
            UUID customerId
    );

    // Get customer bookings by status
    List<Booking> findAllByCustomer_CustomerIdAndStatus(
            UUID customerId,
            BookingStatus status
    );

    // Check whether a garage booking is already linked
    boolean existsByGarageBookingId(UUID garageBookingId);
}