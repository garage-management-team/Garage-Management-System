package org.example.garageservicebackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "garage_managers")
public class GarageManager {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "manager_id")
    private UUID managerId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "personnel_id",
            nullable = false,
            unique = true
    )
    private GaragePersonnel personnel;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;
}