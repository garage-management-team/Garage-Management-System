package com.gms.customer_backend.booking.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingCancelRequest {

    @Size(
            max = 1000,
            message = "Cancellation reason cannot exceed 1000 characters"
    )
    private String cancellationReason;
}