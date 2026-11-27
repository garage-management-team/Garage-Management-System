package org.example.garageservicebackend.service;

import lombok.RequiredArgsConstructor;

import org.example.garageservicebackend.dto.request.CreateServiceSlotRequest;
import org.example.garageservicebackend.dto.response.ServiceSlotResponse;
import org.example.garageservicebackend.entity.GarageManager;
import org.example.garageservicebackend.entity.ServiceSlot;
import org.example.garageservicebackend.entity.ServiceType;
import org.example.garageservicebackend.repository.GarageManagerRepository;
import org.example.garageservicebackend.repository.ServiceSlotRepository;
import org.example.garageservicebackend.repository.ServiceTypeRepository;
import org.example.garageservicebackend.dto.request.UpdateServiceSlotRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

import org.example.garageservicebackend.repository.BookingRepository;
import org.example.garageservicebackend.entity.Booking;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceSlotService {

    private final ServiceSlotRepository serviceSlotRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final GarageManagerRepository garageManagerRepository;
    private final BookingRepository bookingRepository;

    @Transactional
    public ServiceSlotResponse createServiceSlot(
            CreateServiceSlotRequest request,
            UUID managerId) {

        // 1. Find the service type
        ServiceType serviceType = serviceTypeRepository
                .findById(request.getServiceTypeId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Service type not found"));

        // 2. Find the garage manager
        GarageManager manager = garageManagerRepository
                .findById(managerId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Garage manager not found"));

        // 3. Check whether the service type is active
        if (!serviceType.isActive()) {
            throw new IllegalArgumentException(
                    "Cannot create a slot for an inactive service type");
        }

        // 4. Validate service duration
        Integer duration = serviceType.getEstimatedDurationMinutes();

        if (duration == null || duration <= 0) {
            throw new IllegalArgumentException(
                    "Service duration must be greater than zero");
        }

        // 5. Validate that the start time is not in the past
        if (request.getSlotDate().isEqual(
                LocalDate.now(ZoneId.of("Asia/Colombo")))
                && request.getStartTime().isBefore(
                LocalTime.now(ZoneId.of("Asia/Colombo")))) {

            throw new IllegalArgumentException(
                    "Start time cannot be in the past");
        }

        // 6. Calculate end time automatically

        LocalTime endTime = request.getStartTime().plusMinutes(duration);

        if (!endTime.isAfter(request.getStartTime())) {
            throw new IllegalArgumentException(
                    "Service slot end time must be after start time. Slots cannot extend beyond midnight.");
        }

        boolean slotOverlaps =
                serviceSlotRepository
                        .existsBySlotDateAndActiveTrueAndStartTimeLessThanAndEndTimeGreaterThan(
                                request.getSlotDate(),
                                endTime,
                                request.getStartTime()
                        );

        if (slotOverlaps) {
            throw new IllegalArgumentException(
                    "Another active slot already exists during the selected time");
        }

        // 7. Set timestamps
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);

        // 8. Create service slot
        ServiceSlot slot = new ServiceSlot();

        slot.setServiceType(serviceType);
        slot.setCreatedBy(manager);
        slot.setSlotDate(request.getSlotDate());
        slot.setStartTime(request.getStartTime());
        slot.setEndTime(endTime);
        slot.setVehicleCapacity(request.getVehicleCapacity());
        slot.setActive(true);
        slot.setCreatedAt(now);
        slot.setUpdatedAt(now);

        // 9. Save service slot
        ServiceSlot savedSlot = serviceSlotRepository.save(slot);

        // 10. Convert entity into response DTO
        return mapToResponse(savedSlot);
    }

    private ServiceSlotResponse mapToResponse(ServiceSlot slot) {

        // 1. Count pending and confirmed bookings
        long bookedVehicles =
                bookingRepository.countBySlotSlotIdAndBookingStatusIn(
                        slot.getSlotId(),
                        List.of(
                                Booking.BookingStatus.PENDING,
                                Booking.BookingStatus.CONFIRMED
                        )
                );

        // 2. Calculate remaining spaces
        long availableSpaces = Math.max(
                0,
                slot.getVehicleCapacity() - bookedVehicles
        );

        // 3. Determine availability status
        String availabilityStatus;

        if (!slot.isActive()) {
            availabilityStatus = "INACTIVE";
        } else if (availableSpaces == 0) {
            availabilityStatus = "FULLY_BOOKED";
        } else if (bookedVehicles == 0) {
            availabilityStatus = "AVAILABLE";
        } else {
            availabilityStatus = "PARTIALLY_BOOKED";
        }

        // 4. Build response
        return ServiceSlotResponse.builder()
                .slotId(slot.getSlotId())
                .serviceTypeId(slot.getServiceType().getServiceTypeId())
                .serviceTypeName(slot.getServiceType().getServiceName())
                .slotDate(slot.getSlotDate())
                .startTime(slot.getStartTime())
                .endTime(slot.getEndTime())
                .vehicleCapacity(slot.getVehicleCapacity())
                .bookedVehicles(bookedVehicles)
                .availableSpaces(availableSpaces)
                .availabilityStatus(availabilityStatus)
                .active(slot.isActive())
                .createdAt(slot.getCreatedAt())
                .updatedAt(slot.getUpdatedAt())
                .build();
    }

    @Transactional(readOnly = true)
    public List<ServiceSlotResponse> getAllServiceSlots() {
        return serviceSlotRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ServiceSlotResponse getServiceSlotById(UUID slotId) {
        ServiceSlot slot = serviceSlotRepository.findById(slotId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Service slot not found"));

        return mapToResponse(slot);
    }
    //update service slot


    @Transactional
    public ServiceSlotResponse updateServiceSlot(
            UUID slotId,
            UpdateServiceSlotRequest request) {

        ServiceSlot slot = serviceSlotRepository.findById(slotId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Service slot not found"));
        long bookedVehicles =
                bookingRepository.countBySlotSlotIdAndBookingStatusIn(
                        slotId,
                        List.of(
                                Booking.BookingStatus.PENDING,
                                Booking.BookingStatus.CONFIRMED
                        )
                );

        boolean hasBookings = bookedVehicles > 0;
        if (hasBookings) {
            boolean serviceTypeChanged =
                    !slot.getServiceType().getServiceTypeId()
                            .equals(request.getServiceTypeId());

            boolean dateChanged =
                    !slot.getSlotDate().equals(request.getSlotDate());

            boolean startTimeChanged =
                    !slot.getStartTime().equals(request.getStartTime());

            if (serviceTypeChanged || dateChanged || startTimeChanged) {
                throw new IllegalArgumentException(
                        "Cannot change service type, date or start time because this slot has existing bookings");
            }
        }
        ServiceType serviceType = serviceTypeRepository
                .findById(request.getServiceTypeId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Service type not found"));

        if (!serviceType.isActive()) {
            throw new IllegalArgumentException(
                    "Cannot update slot with an inactive service type");
        }

        Integer duration = serviceType.getEstimatedDurationMinutes();

        if (duration == null || duration <= 0) {
            throw new IllegalArgumentException(
                    "Service duration must be greater than zero");
        }


        LocalDate today = LocalDate.now(ZoneId.of("Asia/Colombo"));

        if (request.getSlotDate().isEqual(today)
                && request.getStartTime().isBefore(LocalTime.now(ZoneId.of("Asia/Colombo")))) {
            throw new IllegalArgumentException("Start time cannot be in the past");
        }

        LocalTime endTime = request.getStartTime().plusMinutes(duration);

        if (!endTime.isAfter(request.getStartTime())) {
            throw new IllegalArgumentException(
                    "ervice slot end time must be after start time. Slots cannot extend beyond midnight");
        }


        boolean slotOverlaps =
                serviceSlotRepository
                        .existsBySlotDateAndActiveTrueAndStartTimeLessThanAndEndTimeGreaterThanAndSlotIdNot(
                                request.getSlotDate(),
                                endTime,
                                request.getStartTime(),
                                slotId
                        );

        if (slotOverlaps) {
            throw new IllegalArgumentException(
                    "Another active slot already exists during the selected time");
        }

        if (request.getVehicleCapacity() < bookedVehicles) {
            throw new IllegalArgumentException(
                    "Vehicle capacity cannot be less than the number of existing bookings");
        }

        slot.setServiceType(serviceType);
        slot.setSlotDate(request.getSlotDate());
        slot.setStartTime(request.getStartTime());
        slot.setEndTime(endTime);
        slot.setVehicleCapacity(request.getVehicleCapacity());
        slot.setUpdatedAt(OffsetDateTime.now(ZoneOffset.UTC));

        ServiceSlot updatedSlot = serviceSlotRepository.save(slot);

        return mapToResponse(updatedSlot);
    }

    @Transactional
    public void deactivateServiceSlot(UUID slotId) {

        ServiceSlot slot = serviceSlotRepository.findById(slotId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Service slot not found"));

        if (!slot.isActive()) {
            throw new IllegalArgumentException("Service slot is already inactive");
        }
        long bookedVehicles =
                bookingRepository.countBySlotSlotIdAndBookingStatusIn(
                        slotId,
                        List.of(
                                Booking.BookingStatus.PENDING,
                                Booking.BookingStatus.CONFIRMED
                        )
                );

        if (bookedVehicles > 0) {
            throw new IllegalArgumentException(
                    "Cannot deactivate a service slot with existing pending or confirmed bookings");
        }

        slot.setActive(false);
        slot.setUpdatedAt(OffsetDateTime.now(ZoneOffset.UTC));

        serviceSlotRepository.save(slot);
    }

    @Transactional(readOnly = true)
    public List<ServiceSlotResponse> getAvailableServiceSlots(
            UUID serviceTypeId,
            LocalDate slotDate) {

        LocalDate today = LocalDate.now(ZoneId.of("Asia/Colombo"));
        LocalTime currentTime = LocalTime.now(ZoneId.of("Asia/Colombo"));

        return serviceSlotRepository
                .findByServiceTypeServiceTypeIdAndSlotDateAndActiveTrue(
                        serviceTypeId,
                        slotDate
                )
                .stream()
                .map(this::mapToResponse)
                .filter(slot -> slot.getAvailableSpaces() > 0)
                .filter(slot -> {
                    if (slotDate.isEqual(today)) {
                        return slot.getStartTime().isAfter(currentTime);
                    }
                    return slotDate.isAfter(today);
                })
                .toList();
    }
}