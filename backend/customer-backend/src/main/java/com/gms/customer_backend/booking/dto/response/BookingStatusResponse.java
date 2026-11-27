package com.gms.customer.booking.dto.response;

import com.gms.customer_backend.booking.entity.BookingStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class BookingStatusResponse {

    private UUID bookingId;

    private BookingStatus status;

    private String message;
}