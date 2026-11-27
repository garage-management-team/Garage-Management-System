package com.gms.customer_backend.booking.kafka.event;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record BookingCreatedEvent(

        UUID bookingId,

        UUID customerId,

        UUID vehicleId,

        UUID serviceId,

        String serviceName,

        UUID garageSlotId,

        LocalDate bookingDate,

        LocalTime startTime,

        LocalTime endTime,

        String customerNote

) {
}