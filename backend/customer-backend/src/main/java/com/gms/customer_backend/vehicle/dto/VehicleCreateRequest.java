package com.gms.customer_backend.vehicle.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehicleCreateRequest {

    @NotBlank(message = "Registration number is required")
    @Size(max = 20, message = "Registration number cannot exceed 20 characters")
    private String registrationNumber;

    @NotBlank(message = "Brand is required")
    @Size(max = 100)
    private String brand;

    @NotBlank(message = "Model is required")
    @Size(max = 100)
    private String model;

    @Min(value = 1886, message = "Invalid manufacturing year")
    @Max(value = 2100, message = "Invalid manufacturing year")
    private Integer manufactureYear;

    @NotBlank(message = "Vehicle type is required")
    @Pattern(
            regexp = "CAR|SUV|VAN|MOTORCYCLE|THREE_WHEELER|TRUCK",
            message = "Invalid vehicle type"
    )
    private String vehicleType;

    @Size(max = 50)
    private String color;
}