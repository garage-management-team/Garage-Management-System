package org.example.garageservicebackend.grpc;

import com.gms.grpc.availability.AvailabilityRequest;
import com.gms.grpc.availability.AvailabilityResponse;
import com.gms.grpc.availability.AvailableSlot;
import com.gms.grpc.availability.GarageAvailabilityServiceGrpc;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import lombok.RequiredArgsConstructor;

import org.example.garageservicebackend.dto.response.ServiceSlotResponse;
import org.example.garageservicebackend.service.ServiceSlotService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GarageAvailabilityGrpcService
        extends GarageAvailabilityServiceGrpc.GarageAvailabilityServiceImplBase {

    private final ServiceSlotService serviceSlotService;

    @Override
    public void getAvailableSlots(
            AvailabilityRequest request,
            StreamObserver<AvailabilityResponse> responseObserver) {

        try {
            // 1. Extract and convert request data
            UUID serviceTypeId = UUID.fromString(request.getServiceId());
            LocalDate slotDate = LocalDate.parse(request.getDate());

            // 2. Get available slots using existing business logic
            List<ServiceSlotResponse> availableSlots =
                    serviceSlotService.getAvailableServiceSlots(
                            serviceTypeId,
                            slotDate
                    );

            // 3. Convert ServiceSlotResponse objects into Protobuf messages
            List<AvailableSlot> grpcSlots = availableSlots.stream()
                    .map(this::mapToGrpcSlot)
                    .toList();

            // 4. Build the gRPC response
            AvailabilityResponse response = AvailabilityResponse.newBuilder()
                    .addAllSlots(grpcSlots)
                    .build();

            // 5. Send response to Customer Backend
            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (IllegalArgumentException exception) {

            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription(exception.getMessage())
                            .asRuntimeException()
            );
        } catch (Exception exception) {

            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Unable to retrieve available service slots.")
                            .asRuntimeException()
            );
        }
    }

    private AvailableSlot mapToGrpcSlot(ServiceSlotResponse slot) {

        return AvailableSlot.newBuilder()
                .setSlotId(slot.getSlotId().toString())
                .setServiceId(slot.getServiceTypeId().toString())
                .setServiceName(slot.getServiceTypeName())
                .setDate(slot.getSlotDate().toString())
                .setStartTime(slot.getStartTime().toString())
                .setEndTime(slot.getEndTime().toString())
                .setAvailableCapacity(slot.getAvailableSpaces().intValue())
                .build();
    }
}