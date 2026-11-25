package com.gms.customer_backend.vehicle.service;

import com.gms.customer_backend.customer.entity.Customer;
import com.gms.customer_backend.customer.service.CustomerService;
import com.gms.customer_backend.vehicle.dto.VehicleCreateRequest;
import com.gms.customer_backend.vehicle.dto.VehicleResponse;
import com.gms.customer_backend.vehicle.entity.Vehicle;
import com.gms.customer_backend.vehicle.repository.VehicleRepository;
import com.gms.customer_backend.common.exception.VehicleNotFoundException;
import com.gms.customer_backend.vehicle.dto.VehicleUpdateRequest;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final CustomerService customerService;

    // Add new vehicle
    public VehicleResponse addVehicle(VehicleCreateRequest request) {

        Customer customer = customerService.getCurrentCustomer();

        String registrationNumber =
                request.getRegistrationNumber().trim().toUpperCase();

        if (vehicleRepository.existsByRegistrationNumber(registrationNumber)) {
            throw new IllegalArgumentException(
                    "Vehicle registration number already exists"
            );
        }

        Vehicle vehicle = new Vehicle();

        vehicle.setCustomer(customer);
        vehicle.setRegistrationNumber(registrationNumber);
        vehicle.setBrand(request.getBrand().trim());
        vehicle.setModel(request.getModel().trim());
        vehicle.setManufactureYear(request.getManufactureYear());
        vehicle.setVehicleType(request.getVehicleType());
        vehicle.setColor(request.getColor());

        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        return mapToResponse(savedVehicle);
    }

    // Get all vehicles belonging to logged-in customer
    public List<VehicleResponse> getMyVehicles() {

        Customer customer = customerService.getCurrentCustomer();

        List<Vehicle> vehicles =
                vehicleRepository.findAllByCustomer_CustomerId(
                        customer.getCustomerId()
                );

        return vehicles.stream()
                .map(this::mapToResponse)
                .toList();
    }


    public VehicleResponse getVehicleById(UUID vehicleId) {

        Customer customer = customerService.getCurrentCustomer();

        Vehicle vehicle = vehicleRepository
                .findByVehicleIdAndCustomer_CustomerId(
                        vehicleId,
                        customer.getCustomerId()
                )
                .orElseThrow(() -> new VehicleNotFoundException(
                        "Vehicle not found"
                ));

        return mapToResponse(vehicle);
    }

    // Convert Vehicle entity into VehicleResponse
    private VehicleResponse mapToResponse(Vehicle vehicle) {

        return new VehicleResponse(
                vehicle.getVehicleId(),
                vehicle.getRegistrationNumber(),
                vehicle.getBrand(),
                vehicle.getModel(),
                vehicle.getManufactureYear(),
                vehicle.getVehicleType(),
                vehicle.getColor(),
                vehicle.getCreatedAt(),
                vehicle.getUpdatedAt()
        );
    }

    public VehicleResponse updateVehicle(
            UUID vehicleId,
            VehicleUpdateRequest request) {

        Customer customer = customerService.getCurrentCustomer();

        Vehicle vehicle = vehicleRepository
                .findByVehicleIdAndCustomer_CustomerId(
                        vehicleId,
                        customer.getCustomerId()
                )
                .orElseThrow(() -> new VehicleNotFoundException(
                        "Vehicle not found"
                ));

        vehicle.setBrand(request.getBrand().trim());
        vehicle.setModel(request.getModel().trim());
        vehicle.setManufactureYear(request.getManufactureYear());
        vehicle.setVehicleType(request.getVehicleType());
        vehicle.setColor(request.getColor());

        Vehicle updatedVehicle = vehicleRepository.save(vehicle);

        return mapToResponse(updatedVehicle);
    }

    public void deleteVehicle(UUID vehicleId) {

        Customer customer = customerService.getCurrentCustomer();

        Vehicle vehicle = vehicleRepository
                .findByVehicleIdAndCustomer_CustomerId(
                        vehicleId,
                        customer.getCustomerId()
                )
                .orElseThrow(() -> new VehicleNotFoundException(
                        "Vehicle not found"
                ));

        vehicleRepository.delete(vehicle);
    }

}