package com.gms.customer_backend.booking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
public class AvailableSlotResponse {

    private LocalTime startTime;

    private LocalTime endTime;
}