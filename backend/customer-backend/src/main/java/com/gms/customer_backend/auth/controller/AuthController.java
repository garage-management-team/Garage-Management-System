package com.gms.customer_backend.auth.controller;

import com.gms.customer_backend.auth.dto.request.CustomerRegistrationRequest;
import com.gms.customer_backend.auth.dto.response.CustomerRegistrationResponse;
import com.gms.customer_backend.auth.service.AuthService;
import com.gms.customer_backend.common.response.ApiResponse;
import com.gms.customer_backend.customer.entity.Customer;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<CustomerRegistrationResponse>> registerCustomer(
            @Valid @RequestBody CustomerRegistrationRequest request) {

        Customer customer = authService.registerCustomer(request);

        CustomerRegistrationResponse registrationResponse =
                new CustomerRegistrationResponse(
                        customer.getCustomerId(),
                        customer.getFirstName(),
                        customer.getLastName(),
                        customer.getEmail(),
                        customer.getPhone()
                );

        ApiResponse<CustomerRegistrationResponse> response =
                new ApiResponse<>(
                        true,
                        "Customer registered successfully",
                        registrationResponse
                );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}