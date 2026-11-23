package com.gms.customer_backend.auth.service;

import com.gms.customer_backend.auth.dto.request.CustomerRegistrationRequest;
import com.gms.customer_backend.customer.entity.Customer;
import com.gms.customer_backend.customer.repository.CustomerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gms.customer_backend.common.exception.EmailAlreadyExistsException;

@Service
public class AuthService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            CustomerRepository customerRepository,
            PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Customer registerCustomer(CustomerRegistrationRequest request) {

        // 1. Normalize email
        String normalizedEmail = request.getEmail().trim().toLowerCase();

        // 2. Check whether email already exists
        if (customerRepository.existsByEmail(normalizedEmail)) {
            throw new EmailAlreadyExistsException("Email is already registered");
        }

        // 3. Hash password
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        // 4. Create Customer entity
        Customer customer = new Customer();
        customer.setFirstName(request.getFirstName().trim());
        customer.setLastName(request.getLastName().trim());
        customer.setEmail(normalizedEmail);
        customer.setPhone(request.getPhone());
        customer.setPasswordHash(hashedPassword);

        // 5. Save customer to database
        return customerRepository.save(customer);
    }
}