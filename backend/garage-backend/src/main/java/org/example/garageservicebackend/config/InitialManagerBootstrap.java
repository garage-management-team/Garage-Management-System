package org.example.garageservicebackend.config;

import org.example.garageservicebackend.entity.GarageManager;
import org.example.garageservicebackend.entity.GaragePersonnel;
import org.example.garageservicebackend.repository.GarageManagerRepository;
import org.example.garageservicebackend.repository.GaragePersonnelRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Component
public class InitialManagerBootstrap implements CommandLineRunner {

    private final GaragePersonnelRepository personnelRepository;
    private final GarageManagerRepository managerRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.bootstrap.manager.enabled:false}")
    private boolean bootstrapEnabled;

    @Value("${app.bootstrap.manager.full-name:}")
    private String fullName;

    @Value("${app.bootstrap.manager.email:}")
    private String email;

    @Value("${app.bootstrap.manager.phone:}")
    private String phoneNumber;

    @Value("${app.bootstrap.manager.password:}")
    private String password;

    public InitialManagerBootstrap(
            GaragePersonnelRepository personnelRepository,
            GarageManagerRepository managerRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.personnelRepository = personnelRepository;
        this.managerRepository = managerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {

        if (!bootstrapEnabled) {
            return;
        }

        var existingPersonnel = personnelRepository.findByEmail(email);

        if (existingPersonnel.isPresent()) {

            GaragePersonnel personnel = existingPersonnel.get();

            if (personnel.getPersonnelType()
                    != GaragePersonnel.PersonnelType.MANAGER) {
                throw new IllegalStateException(
                        "Bootstrap email already belongs to a non-manager account"
                );
            }

            if (managerRepository.findByPersonnelPersonnelId(
                    personnel.getPersonnelId()
            ).isEmpty()) {
                throw new IllegalStateException(
                        "Manager personnel record exists without a manager account"
                );
            }

            System.out.println("Initial manager already exists. Skipping creation.");
            return;
        }

        if (fullName.isBlank() || email.isBlank() || password.isBlank()) {
            throw new IllegalStateException(
                    "Initial manager configuration is incomplete"
            );
        }

        OffsetDateTime now = OffsetDateTime.now();

        GaragePersonnel personnel = new GaragePersonnel();
        personnel.setPersonnelId(UUID.randomUUID());
        personnel.setFullName(fullName);
        personnel.setEmail(email);
        personnel.setPhoneNumber(phoneNumber);
        personnel.setPersonnelType(GaragePersonnel.PersonnelType.MANAGER);
        personnel.setActive(true);
        personnel.setCreatedAt(now);
        personnel.setUpdatedAt(now);

        GaragePersonnel savedPersonnel =
                personnelRepository.save(personnel);

        GarageManager manager = new GarageManager();
        manager.setPersonnel(savedPersonnel);
        manager.setPasswordHash(passwordEncoder.encode(password));

        managerRepository.save(manager);

        System.out.println("Initial garage manager created successfully.");
    }
}