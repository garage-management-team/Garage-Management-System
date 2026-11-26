package com.gms.customer_backend.booking.controller;

import com.gms.customer_backend.booking.dto.request.AvailableSlotRequest;
import com.gms.customer_backend.booking.dto.response.AvailableSlotResponse;
import com.gms.customer_backend.booking.service.BookingService;
import com.gms.customer_backend.common.response.ApiResponse;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

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
}