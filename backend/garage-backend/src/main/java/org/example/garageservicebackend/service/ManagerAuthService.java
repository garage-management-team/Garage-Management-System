package org.example.garageservicebackend.service;

import org.example.garageservicebackend.dto.request.ManagerRegistrationRequest;
import org.example.garageservicebackend.dto.response.ManagerRegistrationResponse;
import org.example.garageservicebackend.entity.GarageManager;
import org.example.garageservicebackend.entity.GaragePersonnel;
import org.example.garageservicebackend.repository.GarageManagerRepository;
import org.example.garageservicebackend.repository.GaragePersonnelRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.example.garageservicebackend.security.JwtService;
import org.example.garageservicebackend.dto.request.ManagerLoginRequest;
import org.example.garageservicebackend.dto.response.ManagerLoginResponse;
import org.example.garageservicebackend.security.ManagerUserDetails;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class ManagerAuthService {

    private final GaragePersonnelRepository garagePersonnelRepository;
    private final GarageManagerRepository garageManagerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public ManagerAuthService(
            GaragePersonnelRepository garagePersonnelRepository,
            GarageManagerRepository garageManagerRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService
    ) {
        this.garagePersonnelRepository = garagePersonnelRepository;
        this.garageManagerRepository = garageManagerRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    private void validateEmailNotExists(String email) {

        if (garagePersonnelRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "A manager with this email already exists"
            );
        }
    }
    public Authentication authenticateManager(String email, String password) {

        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
    }

    @Transactional
    public ManagerRegistrationResponse registerManager(
            ManagerRegistrationRequest request
    ) {

        // 1. Check whether the email already exists
        validateEmailNotExists(request.getEmail());

        // 2. Get the current timestamp
        OffsetDateTime now = OffsetDateTime.now();

        // 3. Create personnel information
        GaragePersonnel personnel = new GaragePersonnel();

        personnel.setPersonnelId(UUID.randomUUID());
        personnel.setFullName(request.getFullName());
        personnel.setEmail(request.getEmail());
        personnel.setPhoneNumber(request.getPhoneNumber());
        personnel.setPersonnelType(GaragePersonnel.PersonnelType.MANAGER);
        personnel.setActive(true);
        personnel.setCreatedAt(now);
        personnel.setUpdatedAt(now);

        // 4. Save personnel information
        GaragePersonnel savedPersonnel =
                garagePersonnelRepository.save(personnel);

        // 5. Encode the password using BCrypt
        String encodedPassword =
                passwordEncoder.encode(request.getPassword());

        // 6. Create manager information
        GarageManager manager = new GarageManager();

        manager.setPersonnel(savedPersonnel);
        manager.setPasswordHash(encodedPassword);

        // 7. Save manager information
        GarageManager savedManager =
                garageManagerRepository.save(manager);

        // 8. Return registration response
        return new ManagerRegistrationResponse(
                savedManager.getManagerId(),
                savedPersonnel.getFullName(),
                savedPersonnel.getEmail(),
                "Manager registered successfully"
        );
    }

    public ManagerLoginResponse loginManager(ManagerLoginRequest request) {

        // 1. Authenticate manager credentials
        Authentication authentication = authenticateManager(
                request.getEmail(),
                request.getPassword()
        );

        // 2. Get authenticated manager details
        ManagerUserDetails managerDetails =
                (ManagerUserDetails) authentication.getPrincipal();

        // 3. Generate JWT
        String token = jwtService.generateToken(managerDetails);

        // 4. Return login response
        return new ManagerLoginResponse(
                managerDetails.getManagerId(),
                managerDetails.getFullName(),
                managerDetails.getEmail(),
                token,
                "Bearer",
                jwtService.getJwtExpiration()
        );
    }




}