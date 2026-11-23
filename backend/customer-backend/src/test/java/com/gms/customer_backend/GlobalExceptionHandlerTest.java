package com.gms.customer_backend.common.exception;

import com.gms.customer_backend.common.response.ApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void shouldHandleResourceNotFoundException() {

        ResourceNotFoundException exception =
                new ResourceNotFoundException("Vehicle not found");

        ResponseEntity<ApiResponse<Void>> response =
                handler.handleResourceNotFound(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Vehicle not found", response.getBody().getMessage());
    }

    @Test
    void shouldHandleUnexpectedException() {

        Exception exception = new Exception("Database error");

        ResponseEntity<ApiResponse<Void>> response =
                handler.handleGlobalException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals(
                "An unexpected error occurred",
                response.getBody().getMessage()
        );
    }
}