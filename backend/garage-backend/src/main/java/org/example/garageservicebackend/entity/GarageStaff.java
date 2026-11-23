package org.example.garageservicebackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "garage_staff")
public class GarageStaff {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "staff_id")
    private UUID staffId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "personnel_id",
            nullable = false,
            unique = true
    )
    private GaragePersonnel personnel;

    @Column(name = "specialization", length = 100)
    private String specialization;
}