package com.gms.customer_backend.customer.dto;

import lombok.Getter;

@Getter
public class CustomerProfileResponse {

    private final String customerId;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phone;

    public CustomerProfileResponse(
            String customerId,
            String firstName,
            String lastName,
            String email,
            String phone
    ) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }
}