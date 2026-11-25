package org.example.garageservicebackend.security;

import org.example.garageservicebackend.entity.GaragePersonnel;
import org.example.garageservicebackend.entity.GarageManager;
import org.example.garageservicebackend.repository.GarageManagerRepository;
import org.example.garageservicebackend.repository.GaragePersonnelRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ManagerUserDetailsService implements UserDetailsService {

    private final GaragePersonnelRepository personnelRepository;
    private final GarageManagerRepository managerRepository;

    public ManagerUserDetailsService(
            GaragePersonnelRepository personnelRepository,
            GarageManagerRepository managerRepository
    ) {
        this.personnelRepository = personnelRepository;
        this.managerRepository = managerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        // 1. Find personnel using email
        GaragePersonnel personnel = personnelRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Manager not found with email: " + email
                        )
                );

        // 2. Check whether the account belongs to a manager
        if (personnel.getPersonnelType()
                != GaragePersonnel.PersonnelType.MANAGER) {

            throw new UsernameNotFoundException(
                    "No manager account found with this email"
            );
        }

        // 3. Find the corresponding manager account
        GarageManager manager = managerRepository
                .findByPersonnelPersonnelId(personnel.getPersonnelId())
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Manager account not found"
                        )
                );

        // 4. Return Spring Security user details
        return new ManagerUserDetails(manager);
    }
}