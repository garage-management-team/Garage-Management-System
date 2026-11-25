package com.gms.customer_backend.vehicle.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class VehicleResponse {

    private UUID vehicleId;
    private String registrationNumber;
    private String brand;
    private String model;
    private Integer year;
    private String color;
    private String VehicleType;
    private Instant createdAt;
    private Instant updatedAt;
}