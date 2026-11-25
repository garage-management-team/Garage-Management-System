package org.example.garageservicebackend.security;

import org.example.garageservicebackend.entity.GarageManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class ManagerUserDetails implements UserDetails {

    private final GarageManager manager;

    public ManagerUserDetails(GarageManager manager) {
        this.manager = manager;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority("ROLE_MANAGER")
        );
    }

    @Override
    public String getPassword() {
        return manager.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return manager.getPersonnel().getEmail();
    }

    @Override
    public boolean isEnabled() {
        return manager.getPersonnel().isActive();
    }

    public String getFullName() {
        return manager.getPersonnel().getFullName();
    }

    public String getEmail() {
        return manager.getPersonnel().getEmail();
    }

    public UUID getManagerId() {
        return manager.getManagerId();
    }
}