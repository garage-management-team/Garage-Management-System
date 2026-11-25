package com.gms.customer_backend.auth.service;

import com.gms.customer_backend.auth.dto.CustomerRegistrationRequest;
import com.gms.customer_backend.auth.dto.CustomerLoginRequest;
import com.gms.customer_backend.auth.dto.CustomerLoginResponse;
import com.gms.customer_backend.auth.dto.CustomerLoginResult;
import com.gms.customer_backend.customer.entity.Customer;
import com.gms.customer_backend.customer.repository.CustomerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gms.customer_backend.common.exception.EmailAlreadyExistsException;
import com.gms.customer_backend.common.exception.ResourceNotFoundException;
import com.gms.customer_backend.common.exception.InvalidCredentialsException;
import com.gms.customer_backend.auth.service.JwtService;
import lombok.RequiredArgsConstructor;
import com.gms.customer_backend.auth.dto.CustomerLoginResult;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

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
    public CustomerLoginResult loginCustomer(CustomerLoginRequest request) {

        String normalizedEmail = request.getEmail().trim().toLowerCase();

        Customer customer = customerRepository.findByEmail(normalizedEmail)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Invalid email or password"));

        if (!passwordEncoder.matches(
                request.getPassword(),
                customer.getPasswordHash())) {

            throw new InvalidCredentialsException("Invalid email or password");
        }

        // Generate JWT
        String token = jwtService.generateToken(customer);

        // Create customer response
        CustomerLoginResponse customerResponse = new CustomerLoginResponse(
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail()
        );

        // Return customer details and token internally
        return new CustomerLoginResult(customerResponse, token);
    }
}