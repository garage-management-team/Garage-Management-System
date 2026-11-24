package com.gms.customer_backend.auth.dto;

public class CustomerLoginResult {

    private final CustomerLoginResponse customer;
    private final String token;

    public CustomerLoginResult(CustomerLoginResponse customer, String token) {
        this.customer = customer;
        this.token = token;
    }
    public CustomerLoginResponse getCustomer() {
        return customer;
    }

    public String getToken() {
        return token;
    }
}