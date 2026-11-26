package com.gms.customer_backend.booking.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class GarageServiceResponse {

    private UUID serviceId;

    private String serviceName;

    private String description;

    private Integer estimatedDurationMinutes;

    private BigDecimal price;
}