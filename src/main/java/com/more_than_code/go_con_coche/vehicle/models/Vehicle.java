package com.more_than_code.go_con_coche.vehicle.models;

import com.more_than_code.go_con_coche.owner_profile.OwnerProfile;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.HashSet;

@Entity
@Table(name = "vehicles")
@Data
@AllArgsConstructor @NoArgsConstructor
@Builder
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String vin;

    @Column(nullable = false, unique = true)
    private String plateNumber;

    @Column (nullable = false, unique = true)
    private String insuranceNumber;

    @Column (nullable = false)
    private String model;

    @Column (nullable = false)
    private String brand;

    @Column (nullable = false)
    private Integer year;

    @Column (nullable = false)
    private String color;

    @Enumerated(EnumType.STRING)
    @Column (nullable = false)
    private Seater seater;

    @Column
    private Integer childSeatsNumber;

    @Enumerated(EnumType.STRING)
    @Column (nullable = false)
    private FuelTypeCar fuelTypeCar;

    @Column (nullable = false)
    private String fuelConsumption;

    @Column (nullable = false)
    private String imageUrl;

    private String publicImageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private OwnerProfile owner;
}