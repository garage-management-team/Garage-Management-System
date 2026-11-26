package com.gms.customer_backend.booking.grpc;

import com.gms.customer_backend.customer.entity.Customer;
import com.gms.customer_backend.customer.repository.CustomerRepository;
import com.gms.customer_backend.vehicle.entity.Vehicle;
import com.gms.customer_backend.vehicle.repository.VehicleRepository;

import com.gms.grpc.customer.*;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CustomerInformationGrpcService
        extends CustomerInformationServiceGrpc.CustomerInformationServiceImplBase {

    private final CustomerRepository customerRepository;
    private final VehicleRepository vehicleRepository;

    @Override
    @Transactional(readOnly = true)
    public void getBookingCustomerDetails(
            BookingCustomerRequest request,
            StreamObserver<BookingCustomerResponse> responseObserver) {

        try {
            UUID customerId = UUID.fromString(request.getCustomerId());
            UUID vehicleId = UUID.fromString(request.getVehicleId());

            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));

            Vehicle vehicle = vehicleRepository
                    .findByVehicleIdAndCustomer_CustomerId(vehicleId, customerId)
                    .orElseThrow(() -> new RuntimeException("Vehicle not found"));

            CustomerInformation customerInformation =
                    CustomerInformation.newBuilder()
                            .setName(customer.getFirstName() + " " + customer.getLastName())
                            .setEmail(customer.getEmail())
                            .setPhone(customer.getPhone() != null ? customer.getPhone() : "")
                            .build();

            VehicleInformation vehicleInformation =
                    VehicleInformation.newBuilder()
                            .setRegistrationNumber(vehicle.getRegistrationNumber())
                            .setModel(vehicle.getModel())
                            .setVehicleType(vehicle.getVehicleType())
                            .setManufactureYear(vehicle.getManufactureYear())
                            .build();

            BookingCustomerResponse response =
                    BookingCustomerResponse.newBuilder()
                            .setCustomer(customerInformation)
                            .setVehicle(vehicleInformation)
                            .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (IllegalArgumentException exception) {

            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription("Invalid customer or vehicle ID")
                            .asRuntimeException()
            );

        } catch (RuntimeException exception) {

            if ("Customer not found".equals(exception.getMessage())
                    || "Vehicle not found".equals(exception.getMessage())) {

                responseObserver.onError(
                        Status.NOT_FOUND
                                .withDescription(exception.getMessage())
                                .asRuntimeException()
                );

            } else {
                responseObserver.onError(
                        Status.INTERNAL
                                .withDescription("Unable to retrieve booking information")
                                .asRuntimeException()
                );
            }
        }
    }
}