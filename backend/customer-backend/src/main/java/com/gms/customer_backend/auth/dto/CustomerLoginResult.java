package com.gms.customer_backend.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerLoginResult {

    private final CustomerLoginResponse customer;
    private final String token;

    public CustomerLoginResult(CustomerLoginResponse customer, String token) {
        this.customer = customer;
        this.token = token;
    }
}