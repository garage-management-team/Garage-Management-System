package com.gms.customer_backend.customer.controller;

import com.gms.customer_backend.customer.entity.Customer;
import com.gms.customer_backend.customer.dto.CustomerProfileResponse;
import com.gms.customer_backend.customer.service.CustomerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.gms.customer_backend.customer.dto.CustomerProfileUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/customers")
public class CustomerProfileController {

    private final CustomerService customerService;

    public CustomerProfileController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/me")
    public CustomerProfileResponse getMyProfile() {
        return customerService.getCurrentCustomerProfile();
    }
    @PutMapping("/me")
    public ResponseEntity<CustomerProfileResponse> updateMyProfile(
            @Valid @RequestBody CustomerProfileUpdateRequest request) {

        CustomerProfileResponse updatedProfile =
                customerService.updateCurrentCustomerProfile(request);

        return ResponseEntity.ok(updatedProfile);
    }
}