package com.gms.customer_backend.booking.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

import java.util.UUID;

@Getter
@Setter
public class BookingCreateRequest {

    @NotNull(message = "Vehicle ID is required")
    private UUID vehicleId;

    @NotNull(message = "Service ID is required")
    private UUID serviceId;

    @NotNull(message = "Slot ID is required")
    private UUID slotId;

    @NotNull(message = "Booking date is required")
    @FutureOrPresent(message = "Booking date cannot be in the past")
    private LocalDate bookingDate;

    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    private LocalTime endTime;

    @Size(max = 1000,
            message = "Customer note cannot exceed 1000 characters")
    private String customerNote;

    @AssertTrue(message = "End time must be later than start time")
    @JsonIgnore
    public boolean isValidTimeRange() {

        if (startTime == null || endTime == null) {
            return true;
        }

        return endTime.isAfter(startTime);
    }

    @AssertTrue(message = "Booking time must be in the future")
    @JsonIgnore
    public boolean isBookingTimeValid() {

        if (bookingDate == null || startTime == null) {
            return true;
        }

        if (bookingDate.isAfter(LocalDate.now())) {
            return true;
        }

        return LocalDateTime.of(bookingDate, startTime)
                .isAfter(LocalDateTime.now());
    }
}