package com.gms.customer_backend.booking.service;


import com.gms.customer_backend.booking.dto.request.BookingCreateRequest;
import com.gms.customer_backend.booking.dto.response.BookingResponse;
import com.gms.customer_backend.booking.entity.Booking;
import com.gms.customer_backend.booking.entity.BookingStatus;
import com.gms.customer_backend.booking.repository.BookingRepository;
import com.gms.customer_backend.customer.entity.Customer;
import com.gms.customer_backend.customer.repository.CustomerRepository;
import com.gms.customer_backend.booking.dto.request.AvailableSlotRequest;
import com.gms.customer_backend.booking.dto.response.AvailableSlotResponse;
import com.gms.customer_backend.vehicle.entity.Vehicle;
import com.gms.customer_backend.vehicle.repository.VehicleRepository;
import com.gms.customer_backend.booking.grpc.GarageAvailabilityGrpcClient;
import com.gms.customer_backend.booking.dto.response.GarageServiceResponse;
import com.gms.customer_backend.booking.grpc.GarageServicesGrpcClient;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final VehicleRepository vehicleRepository;
    private final GarageAvailabilityGrpcClient garageAvailabilityGrpcClient;
    private final GarageServicesGrpcClient garageServicesGrpcClient;
    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    public List<AvailableSlotResponse> getAvailableSlots(
            UUID customerId,
            AvailableSlotRequest request
    ) {

        // 1. Validate vehicle ownership
        Vehicle vehicle = vehicleRepository
                .findByVehicleIdAndCustomer_CustomerId(
                        request.getVehicleId(),
                        customerId
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Vehicle not found for this customer"
                        )
                );

        // 2. Request available slots from Garage Backend using gRPC
        return garageAvailabilityGrpcClient.getAvailableSlots(
                request.getServiceId(),
                request.getDate()
        );
    }

    public List<GarageServiceResponse> getActiveServices() {
        return garageServicesGrpcClient.getActiveServices();
    }

    //Booking Creation

    @Transactional
    public BookingResponse createBooking(
            UUID customerId,
            BookingCreateRequest request) {

        // 1. Find customer
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new RuntimeException("Customer not found"));

        // 2. Verify vehicle ownership
        Vehicle vehicle = vehicleRepository
                .findByVehicleIdAndCustomer_CustomerId(
                        request.getVehicleId(),
                        customerId
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Vehicle not found or does not belong to this customer"));

        // 3. Retrieve active services through gRPC
        GarageServiceResponse garageService =
                garageServicesGrpcClient.getActiveServices()
                        .stream()
                        .filter(service ->
                                service.getServiceId()
                                        .equals(request.getServiceId()))
                        .findFirst()
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Selected garage service not found or inactive"));


        // Validate selected booking slot
        AvailableSlotResponse availableSlot =
                validateBookingSlot(request);


        // 4. Create booking entity
        Booking booking = new Booking();

        booking.setCustomer(customer);
        booking.setVehicle(vehicle);

        booking.setServiceId(garageService.getServiceId());
        booking.setServiceName(garageService.getServiceName());

        booking.setGarageSlotId(availableSlot.getSlotId());

        booking.setBookingDate(availableSlot.getDate());
        booking.setStartTime(availableSlot.getStartTime());
        booking.setEndTime(availableSlot.getEndTime());

        booking.setCustomerNote(request.getCustomerNote());

        // Initial booking status
        booking.setStatus(BookingStatus.PROCESSING);

        // 5. Save booking in Customer Database
        Booking savedBooking = bookingRepository.save(booking);

        // 6. Return response
        return mapToResponse(savedBooking);
    }

    private BookingResponse mapToResponse(Booking booking) {

        return new BookingResponse(
                booking.getBookingId(),
                booking.getVehicle().getVehicleId(),
                booking.getServiceId(),
                booking.getServiceName(),
                booking.getGarageSlotId(),
                booking.getBookingDate(),
                booking.getStartTime(),
                booking.getEndTime(),
                booking.getStatus(),
                booking.getCustomerNote(),
                booking.getRejectionReason(),
                booking.getCancellationReason(),
                booking.getCreatedAt(),
                booking.getUpdatedAt()
        );
    }

    private AvailableSlotResponse validateBookingSlot(
            BookingCreateRequest request) {

        // Retrieve available slots from Garage Backend using gRPC
        List<AvailableSlotResponse> availableSlots =
                garageAvailabilityGrpcClient.getAvailableSlots(
                        request.getServiceId(),
                        request.getBookingDate()
                );

        // Find the slot selected by the customer
        return availableSlots.stream()
                .filter(slot ->
                        slot.getSlotId().equals(request.getSlotId())
                )
                .filter(slot ->
                        slot.getServiceId().equals(request.getServiceId())
                )
                .filter(slot ->
                        slot.getDate().equals(request.getBookingDate())
                )
                .filter(slot ->
                        slot.getStartTime().equals(request.getStartTime())
                )
                .filter(slot ->
                        slot.getEndTime().equals(request.getEndTime())
                )
                .filter(slot ->
                        slot.getAvailableCapacity() != null
                                && slot.getAvailableCapacity() > 0
                )
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Selected booking slot is unavailable or invalid."
                        )
                );
    }


}