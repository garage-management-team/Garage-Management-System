package com.gms.customer_backend.common.exception;

public class GarageServiceUnavailableException
        extends RuntimeException {

    public GarageServiceUnavailableException(String message) {
        super(message);
    }
}