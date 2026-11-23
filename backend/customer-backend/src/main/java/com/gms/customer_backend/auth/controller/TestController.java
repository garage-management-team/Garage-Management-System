package com.gms.customer_backend.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/protected")
    public String protectedEndpoint(Authentication authentication) {

        String customerId = authentication.getName();

        return "Authenticated successfully! Customer ID: " + customerId;
    }
}