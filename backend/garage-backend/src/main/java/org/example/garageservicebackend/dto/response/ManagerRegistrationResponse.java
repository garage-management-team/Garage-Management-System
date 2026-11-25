package org.example.garageservicebackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class ManagerRegistrationResponse {

    private UUID managerId;
    private String fullName;
    private String email;
    private String message;
}