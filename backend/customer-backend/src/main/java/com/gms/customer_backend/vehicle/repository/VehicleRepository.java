package com.gms.customer_backend.vehicle.repository;

import com.gms.customer_backend.vehicle.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {

    List<Vehicle> findAllByCustomer_CustomerId(UUID customerId);

    Optional<Vehicle> findByVehicleIdAndCustomer_CustomerId(
            UUID vehicleId,
            UUID customerId
    );

    boolean existsByRegistrationNumber(String registrationNumber);
}