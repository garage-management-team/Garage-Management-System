package com.gms.customer_backend.booking.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
public class AvailableSlotResponse {

    private UUID slotId;

    private UUID serviceId;

    private String serviceName;

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    private Integer availableCapacity;
}