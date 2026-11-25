package org.example.garageservicebackend.controller;

import jakarta.validation.Valid;
import org.example.garageservicebackend.dto.request.ManagerRegistrationRequest;
import org.example.garageservicebackend.dto.response.ManagerRegistrationResponse;
import org.example.garageservicebackend.service.ManagerAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class ManagerAuthController {

    private final ManagerAuthService managerAuthService;

    public ManagerAuthController(ManagerAuthService managerAuthService) {
        this.managerAuthService = managerAuthService;
    }

    @PostMapping("/register")
    public ResponseEntity<ManagerRegistrationResponse> registerManager(
            @Valid @RequestBody ManagerRegistrationRequest request
    ) {

        ManagerRegistrationResponse response =
                managerAuthService.registerManager(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}