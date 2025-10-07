package com.more_than_code.go_con_coche.vehicle_rental_offer.models;

import com.more_than_code.go_con_coche.location.Location;
import com.more_than_code.go_con_coche.owner_profile.OwnerProfile;
import com.more_than_code.go_con_coche.vehicle.models.Vehicle;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "vehicle_rental_offers")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehicleRentalOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Column(name = "start_date_time", nullable = false)
    private LocalDateTime startDateTime;

    @Column(name = "end_date_time", nullable = false)
    private LocalDateTime endDateTime;

    @Column(name = "available",nullable = false)
    private boolean isAvailable;

    @Column(name = "price_hour", nullable = false)
    private double priceHour;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private OwnerProfile owner;

    @OneToMany(mappedBy = "offer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RentalOfferSlot> slots = new ArrayList<>();
}