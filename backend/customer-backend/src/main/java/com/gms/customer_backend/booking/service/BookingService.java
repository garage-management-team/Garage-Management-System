package com.gms.customer_backend.booking.service;

import com.gms.customer_backend.booking.dto.request.AvailableSlotRequest;
import com.gms.customer_backend.booking.dto.response.AvailableSlotResponse;
import com.gms.customer_backend.vehicle.entity.Vehicle;
import com.gms.customer_backend.vehicle.repository.VehicleRepository;
import com.gms.customer_backend.booking.grpc.GarageAvailabilityGrpcClient;
import com.gms.customer_backend.booking.dto.response.GarageServiceResponse;
import com.gms.customer_backend.booking.grpc.GarageServicesGrpcClient;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final VehicleRepository vehicleRepository;
    private final GarageAvailabilityGrpcClient garageAvailabilityGrpcClient;
    private final GarageServicesGrpcClient garageServicesGrpcClient;

    @Transactional(readOnly = true)
    public List<AvailableSlotResponse> getAvailableSlots(
            UUID customerId,
            AvailableSlotRequest request
    ) {

        // 1. Validate vehicle ownership
        Vehicle vehicle = vehicleRepository
                .findByVehicleIdAndCustomer_CustomerId(
                        request.getVehicleId(),
                        customerId
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Vehicle not found for this customer"
                        )
                );

        // 2. Request available slots from Garage Backend using gRPC
        return garageAvailabilityGrpcClient.getAvailableSlots(
                request.getServiceId(),
                request.getDate()
        );
    }

    public List<GarageServiceResponse> getActiveServices() {
        return garageServicesGrpcClient.getActiveServices();
    }


}