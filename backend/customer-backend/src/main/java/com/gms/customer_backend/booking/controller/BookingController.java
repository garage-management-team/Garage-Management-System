package com.gms.customer_backend.booking.controller;

import com.gms.customer_backend.booking.dto.request.AvailableSlotRequest;
import com.gms.customer_backend.booking.dto.response.AvailableSlotResponse;
import com.gms.customer_backend.booking.service.BookingService;
import com.gms.customer_backend.common.response.ApiResponse;
import com.gms.customer_backend.booking.dto.response.GarageServiceResponse;
import jakarta.validation.Valid;
import com.gms.customer_backend.booking.dto.request.BookingCreateRequest;
import com.gms.customer_backend.booking.dto.response.BookingResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    //  Create Booking
    @PostMapping
    public ResponseEntity<ApiResponse<BookingResponse>> createBooking(
            @AuthenticationPrincipal String customerId,
            @Valid @RequestBody BookingCreateRequest request) {

        BookingResponse response = bookingService.createBooking(
                UUID.fromString(customerId),
                request
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        true,
                        "Booking created successfully",
                        response
                ));
    }


    // Check available time slots
    @GetMapping("/available-slots")
    public ResponseEntity<ApiResponse<List<AvailableSlotResponse>>> getAvailableSlots(

            @AuthenticationPrincipal String customerId,

            @Valid @ModelAttribute AvailableSlotRequest request

    ) {

        List<AvailableSlotResponse> slots =
                bookingService.getAvailableSlots(
                        UUID.fromString(customerId),
                        request
                );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Available slots retrieved successfully",
                        slots
                )
        );
    }

    @GetMapping("/services")
    public ResponseEntity<ApiResponse<List<GarageServiceResponse>>> getActiveServices() {

        List<GarageServiceResponse> services =
                bookingService.getActiveServices();

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Active services retrieved successfully",
                        services
                )
        );
    }

}