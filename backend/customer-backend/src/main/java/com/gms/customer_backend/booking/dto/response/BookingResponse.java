package com.gms.customer_backend.booking.dto.response;

import com.gms.customer_backend.booking.entity.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class BookingResponse {

    private UUID bookingId;

    private UUID vehicleId;

    private UUID serviceId;

    private String serviceName;

    private LocalDate bookingDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private BookingStatus status;

    private String customerNote;

    private String rejectionReason;

    private String cancellationReason;

    private Instant createdAt;

    private Instant updatedAt;
}