package com.gms.customer_backend.common.exception;

public class CustomerNotAuthenticatedException extends RuntimeException {

    public CustomerNotAuthenticatedException(String message) {
        super(message);
    }
}