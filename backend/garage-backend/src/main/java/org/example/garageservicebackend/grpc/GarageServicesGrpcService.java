package org.example.garageservicebackend.grpc;

import com.gms.grpc.services.GarageServicesServiceGrpc;
import com.gms.grpc.services.GetServicesRequest;
import com.gms.grpc.services.GetServicesResponse;
import com.gms.grpc.services.ServiceType;
import io.grpc.stub.StreamObserver;
import org.example.garageservicebackend.service.ServiceTypeService;
import org.springframework.stereotype.Service;

@Service
public class GarageServicesGrpcService
        extends GarageServicesServiceGrpc.GarageServicesServiceImplBase {

    private final ServiceTypeService serviceTypeService;

    public GarageServicesGrpcService(ServiceTypeService serviceTypeService) {
        this.serviceTypeService = serviceTypeService;
    }

    @Override
    public void getActiveServices(
            GetServicesRequest request,
            StreamObserver<GetServicesResponse> responseObserver) {

        try {
            var services = serviceTypeService.getActiveServices();

            GetServicesResponse.Builder responseBuilder =
                    GetServicesResponse.newBuilder();

            for (var service : services) {
                ServiceType grpcService = ServiceType.newBuilder()
                        .setServiceId(service.getServiceTypeId().toString())
                        .setServiceName(service.getServiceName())
                        .setDescription(
                                service.getDescription() != null
                                        ? service.getDescription()
                                        : "")
                        .setEstimatedDurationMinutes(
                                service.getEstimatedDurationMinutes())
                        .setPrice(service.getPrice().toPlainString())
                        .build();

                responseBuilder.addServices(grpcService);
            }

            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();

        } catch (Exception exception) {
            responseObserver.onError(
                    io.grpc.Status.INTERNAL
                            .withDescription("Failed to retrieve active services")
                            .withCause(exception)
                            .asRuntimeException()
            );
        }
    }
}