package org.example.garageservicebackend.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Builder
public class ServiceSlotResponse {

    private UUID slotId;

    private UUID serviceTypeId;
    private String serviceTypeName;

    private LocalDate slotDate;

    private LocalTime startTime;
    private LocalTime endTime;

    private Integer vehicleCapacity;

    private Long bookedVehicles;
    private Long availableSpaces;
    private String availabilityStatus;

    private boolean active;

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}