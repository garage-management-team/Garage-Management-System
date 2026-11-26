package org.example.garageservicebackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.example.garageservicebackend.dto.request.CreateServiceSlotRequest;
import org.example.garageservicebackend.dto.response.ServiceSlotResponse;
import org.example.garageservicebackend.security.ManagerUserDetails;
import org.example.garageservicebackend.service.ServiceSlotService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.UUID;
import org.example.garageservicebackend.dto.request.UpdateServiceSlotRequest;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/service-slots")
@RequiredArgsConstructor
public class ServiceSlotController {

    private final ServiceSlotService serviceSlotService;

    @PostMapping
    public ResponseEntity<ServiceSlotResponse> createServiceSlot(
            @Valid @RequestBody CreateServiceSlotRequest request,
            @AuthenticationPrincipal ManagerUserDetails managerDetails) {

        ServiceSlotResponse response =
                serviceSlotService.createServiceSlot(
                        request,
                        managerDetails.getManagerId()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<ServiceSlotResponse>> getAllServiceSlots() {
        List<ServiceSlotResponse> response = serviceSlotService.getAllServiceSlots();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{slotId}")
    public ResponseEntity<ServiceSlotResponse> getServiceSlotById(
            @PathVariable UUID slotId) {

        ServiceSlotResponse response =
                serviceSlotService.getServiceSlotById(slotId);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{slotId}")
    public ResponseEntity<ServiceSlotResponse> updateServiceSlot(
            @PathVariable UUID slotId,
            @Valid @RequestBody UpdateServiceSlotRequest request) {

        ServiceSlotResponse response =
                serviceSlotService.updateServiceSlot(slotId, request);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{slotId}/deactivate")
    public ResponseEntity<Void> deactivateServiceSlot(
            @PathVariable UUID slotId) {

        serviceSlotService.deactivateServiceSlot(slotId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available")
    public ResponseEntity<List<ServiceSlotResponse>> getAvailableServiceSlots(
            @RequestParam UUID serviceTypeId,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {

        List<ServiceSlotResponse> response =
                serviceSlotService.getAvailableServiceSlots(
                        serviceTypeId,
                        date
                );

        return ResponseEntity.ok(response);
    }

}