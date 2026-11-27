package com.gms.customer_backend.booking.grpc;

import com.gms.grpc.services.GarageServicesServiceGrpc;
import com.gms.grpc.services.GetServicesRequest;
import com.gms.grpc.services.GetServicesResponse;
import com.gms.grpc.services.ServiceType;
import com.gms.customer_backend.booking.dto.response.GarageServiceResponse;
import io.grpc.ManagedChannel;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class GarageServicesGrpcClient {

    private final GarageServicesServiceGrpc.GarageServicesServiceBlockingStub blockingStub;

    public GarageServicesGrpcClient(ManagedChannel garageManagedChannel) {
        this.blockingStub =
                GarageServicesServiceGrpc.newBlockingStub(garageManagedChannel);
    }

    public List<GarageServiceResponse> getActiveServices() {

        try {
            GetServicesRequest request =
                    GetServicesRequest.newBuilder().build();

            GetServicesResponse response =
                    blockingStub
                            .withDeadlineAfter(5, TimeUnit.SECONDS)
                            .getActiveServices(request);

            return response.getServicesList()
                    .stream()
                    .map(this::mapToResponse)
                    .toList();

        } catch (StatusRuntimeException exception) {

            Status.Code code = exception.getStatus().getCode();

            if (code == Status.Code.UNAVAILABLE) {
                throw new RuntimeException(
                        "Garage Backend is currently unavailable.");
            }

            if (code == Status.Code.DEADLINE_EXCEEDED) {
                throw new RuntimeException(
                        "Garage Backend took too long to respond.");
            }

            throw new RuntimeException(
                    "Unable to retrieve services from Garage Backend.");
        }
    }

    private GarageServiceResponse mapToResponse(ServiceType service) {

        GarageServiceResponse response = new GarageServiceResponse();

        response.setServiceId(UUID.fromString(service.getServiceId()));
        response.setServiceName(service.getServiceName());
        response.setDescription(service.getDescription());
        response.setEstimatedDurationMinutes(
                service.getEstimatedDurationMinutes());
        response.setPrice(new BigDecimal(service.getPrice()));

        return response;
    }
}