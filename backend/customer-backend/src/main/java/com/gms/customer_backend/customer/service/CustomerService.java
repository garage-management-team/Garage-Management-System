package com.gms.customer_backend.customer.service;

import com.gms.customer_backend.common.exception.CustomerNotAuthenticatedException;
import com.gms.customer_backend.customer.dto.CustomerProfileResponse;
import com.gms.customer_backend.customer.entity.Customer;
import com.gms.customer_backend.customer.repository.CustomerRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.gms.customer_backend.customer.dto.CustomerProfileUpdateRequest;

import java.util.UUID;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // Get the currently authenticated customer's ID
    public String getCurrentCustomerId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new CustomerNotAuthenticatedException("Customer is not authenticated");
        }

        return authentication.getName();
    }

    // Retrieve the currently authenticated customer from the database
    public Customer getCurrentCustomer() {
        String customerId = getCurrentCustomerId();

        return customerRepository.findById(UUID.fromString(customerId))
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    // Convert the Customer entity into a safe profile response DTO
    public CustomerProfileResponse getCurrentCustomerProfile() {
        Customer customer = getCurrentCustomer();

        return new CustomerProfileResponse(
                customer.getCustomerId().toString(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhone()
        );
    }
    public CustomerProfileResponse updateCurrentCustomerProfile(
            CustomerProfileUpdateRequest request) {

        Customer customer = getCurrentCustomer();

        customer.setFirstName(request.getFirstName().trim());
        customer.setLastName(request.getLastName().trim());
        customer.setPhone(request.getPhone().trim());

        Customer updatedCustomer = customerRepository.save(customer);

        return new CustomerProfileResponse(
                updatedCustomer.getCustomerId().toString(),
                updatedCustomer.getFirstName(),
                updatedCustomer.getLastName(),
                updatedCustomer.getEmail(),
                updatedCustomer.getPhone()
        );
    }
}