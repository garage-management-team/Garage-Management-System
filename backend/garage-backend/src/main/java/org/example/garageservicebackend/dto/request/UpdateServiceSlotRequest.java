package org.example.garageservicebackend.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
public class UpdateServiceSlotRequest {

    @NotNull(message = "Service type is required")
    private UUID serviceTypeId;

    @NotNull(message = "Slot date is required")
    @FutureOrPresent(message = "Slot date cannot be in the past")
    private LocalDate slotDate;

    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    @NotNull(message = "Vehicle capacity is required")
    @Min(value = 1, message = "Vehicle capacity must be at least 1")
    private Integer vehicleCapacity;
}