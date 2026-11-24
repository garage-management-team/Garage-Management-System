package com.gms.customer_backend.auth.controller;

import com.gms.customer_backend.auth.dto.CustomerRegistrationRequest;
import com.gms.customer_backend.auth.dto.CustomerRegistrationResponse;
import com.gms.customer_backend.auth.dto.CustomerLoginRequest;
import com.gms.customer_backend.auth.dto.CustomerLoginResult;
import com.gms.customer_backend.auth.dto.CustomerLoginResponse;
import com.gms.customer_backend.auth.service.AuthService;
import com.gms.customer_backend.common.response.ApiResponse;
import com.gms.customer_backend.customer.entity.Customer;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

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
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<CustomerLoginResponse>> login(
            @Valid @RequestBody CustomerLoginRequest request) {

        CustomerLoginResult result = authService.loginCustomer(request);

        ResponseCookie cookie = ResponseCookie.from("ACCESS_TOKEN", result.getToken())
                .httpOnly(true)
                .secure(false) // false for local HTTP development
                .path("/")
                .maxAge(86400)
                .sameSite("Lax")
                .build();

        ApiResponse<CustomerLoginResponse> response = new ApiResponse<>(
                true,
                "Login successful",
                result.getCustomer()
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(response);
    }
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logoutCustomer() {

        ResponseCookie cookie = ResponseCookie.from("ACCESS_TOKEN", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        ApiResponse<Void> response = new ApiResponse<>(
                true,
                "Logout successful",
                null
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(response);
    }
}