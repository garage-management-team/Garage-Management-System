package com.gms.customer_backend.booking.grpc;

import com.gms.grpc.availability.AvailabilityRequest;
import com.gms.grpc.availability.AvailabilityResponse;
import com.gms.grpc.availability.AvailableSlot;
import com.gms.grpc.availability.GarageAvailabilityServiceGrpc;
import com.gms.customer_backend.common.exception.GarageServiceUnavailableException;
import com.gms.customer_backend.booking.dto.response.AvailableSlotResponse;

import io.grpc.ManagedChannel;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import java.util.concurrent.TimeUnit;

@Component
public class GarageAvailabilityGrpcClient {

    private final GarageAvailabilityServiceGrpc.GarageAvailabilityServiceBlockingStub blockingStub;

    public GarageAvailabilityGrpcClient(ManagedChannel garageManagedChannel) {

        this.blockingStub =
                GarageAvailabilityServiceGrpc.newBlockingStub(
                        garageManagedChannel
                );
    }

    public List<AvailableSlotResponse> getAvailableSlots(
            UUID serviceId,
            LocalDate date
    ) {

        try {
            // 1. Create gRPC request
            AvailabilityRequest request =
                    AvailabilityRequest.newBuilder()
                            .setServiceId(serviceId.toString())
                            .setDate(date.toString())
                            .build();

            // 2. Set a 5-second deadline
            AvailabilityResponse response =
                    blockingStub
                            .withDeadlineAfter(5, TimeUnit.SECONDS)
                            .getAvailableSlots(request);

            // 3. Convert response into Customer Backend DTO
            return response.getSlotsList()
                    .stream()
                    .map(this::mapToResponse)
                    .toList();

        } catch (StatusRuntimeException exception) {

            // 4. Handle gRPC errors
            Status.Code code = exception.getStatus().getCode();

            if (code == Status.Code.UNAVAILABLE) {
                throw new GarageServiceUnavailableException(
                        "Garage service is currently unavailable."
                );
            }

            if (code == Status.Code.DEADLINE_EXCEEDED) {
                throw new GarageServiceUnavailableException(
                        "Garage service took too long to respond."
                );
            }

            if (code == Status.Code.INVALID_ARGUMENT) {
                throw new IllegalArgumentException(
                        "Invalid availability request."
                );
            }

            throw new GarageServiceUnavailableException(
                    "Unable to retrieve available slots from Garage Backend."
            );
        }
    }
    // Convert Protocol Buffer response into Java DTO
    private AvailableSlotResponse mapToResponse(AvailableSlot slot) {

        AvailableSlotResponse response = new AvailableSlotResponse();

        response.setSlotId(UUID.fromString(slot.getSlotId()));

        response.setServiceId(UUID.fromString(slot.getServiceId()));

        response.setServiceName(slot.getServiceName());

        response.setDate(LocalDate.parse(slot.getDate()));

        response.setStartTime(LocalTime.parse(slot.getStartTime()));

        response.setEndTime(LocalTime.parse(slot.getEndTime()));

        response.setAvailableCapacity(slot.getAvailableCapacity());

        return response;
    }
}