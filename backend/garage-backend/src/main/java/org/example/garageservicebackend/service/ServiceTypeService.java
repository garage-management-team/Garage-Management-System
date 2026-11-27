package org.example.garageservicebackend.service;

import org.example.garageservicebackend.entity.ServiceType;
import org.example.garageservicebackend.repository.ServiceTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceTypeService {

    private final ServiceTypeRepository serviceTypeRepository;

    public ServiceTypeService(ServiceTypeRepository serviceTypeRepository) {
        this.serviceTypeRepository = serviceTypeRepository;
    }

    public List<ServiceType> getActiveServices() {
        return serviceTypeRepository.findByActiveTrue();
    }
}