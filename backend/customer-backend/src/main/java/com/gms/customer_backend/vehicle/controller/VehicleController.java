package com.gms.customer_backend.vehicle.controller;

import com.gms.customer_backend.vehicle.dto.VehicleCreateRequest;
import com.gms.customer_backend.vehicle.dto.VehicleResponse;
import com.gms.customer_backend.vehicle.service.VehicleService;
import com.gms.customer_backend.common.response.ApiResponse;
import jakarta.validation.Valid;
import com.gms.customer_backend.vehicle.dto.VehicleUpdateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/vehicles")
@AllArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    // Add new vehicle
    @PostMapping
    public ResponseEntity<VehicleResponse> addVehicle(
            @Valid @RequestBody VehicleCreateRequest request) {

        VehicleResponse response = vehicleService.addVehicle(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Get all vehicles belonging to logged-in customer
    @GetMapping
    public ResponseEntity<ApiResponse<List<VehicleResponse>>> getMyVehicles() {

        List<VehicleResponse> vehicles = vehicleService.getMyVehicles();

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Vehicles retrieved successfully", vehicles)
        );
    }
    // Get vehicle by ID
    @GetMapping("/{vehicleId}")
    public ResponseEntity<ApiResponse<VehicleResponse>> getVehicleById(
            @PathVariable UUID vehicleId ) {

        VehicleResponse vehicle = vehicleService.getVehicleById(vehicleId);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Vehicles retrieved successfully", vehicle)
        );
    }

    // Update vehicle
    @PutMapping("/{vehicleId}")
    public ResponseEntity<ApiResponse<VehicleResponse>> updateVehicle(
            @PathVariable UUID vehicleId,
            @Valid @RequestBody VehicleUpdateRequest request) {

        VehicleResponse updatedVehicle =
                vehicleService.updateVehicle(vehicleId, request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Vehicle updated successfully",
                        updatedVehicle
                )
        );
    }


    // Delete vehicle
    @DeleteMapping("/{vehicleId}")
    public ResponseEntity<ApiResponse<Void>> deleteVehicle(
            @PathVariable UUID vehicleId) {

        vehicleService.deleteVehicle(vehicleId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Vehicle deleted successfully",
                        null
                )
        );
    }

}