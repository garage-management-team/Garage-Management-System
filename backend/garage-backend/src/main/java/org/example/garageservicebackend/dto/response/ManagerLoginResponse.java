package org.example.garageservicebackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class ManagerLoginResponse {

    private UUID managerId;
    private String fullName;
    private String email;
    private String accessToken;
    private String tokenType;
    private long expiresIn;
}