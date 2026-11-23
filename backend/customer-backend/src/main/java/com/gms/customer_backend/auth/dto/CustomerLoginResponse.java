package com.gms.customer_backend.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerLoginResponse {

    private String firstName;
    private String lastName;
    private String email;

    public CustomerLoginResponse(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}