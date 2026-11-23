package org.example.garageservicebackend.repository;

import org.example.garageservicebackend.entity.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ServiceTypeRepository
        extends JpaRepository<ServiceType, UUID> {

    List<ServiceType> findByActiveTrue();

    boolean existsByServiceNameIgnoreCase(String serviceName);
}