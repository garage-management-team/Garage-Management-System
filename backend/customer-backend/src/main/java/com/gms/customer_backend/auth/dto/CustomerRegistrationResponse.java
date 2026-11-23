package com.gms.customer_backend.auth.dto.response;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerRegistrationResponse {

    private UUID customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    public CustomerRegistrationResponse() {
    }

    public CustomerRegistrationResponse(
            UUID customerId,
            String firstName,
            String lastName,
            String email,
            String phone) {

        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

}