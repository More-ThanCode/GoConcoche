package com.more_than_code.go_con_coche.vehicle_rental_offer.models;

import com.more_than_code.go_con_coche.location.Location;
import com.more_than_code.go_con_coche.owner_profile.OwnerProfile;
import com.more_than_code.go_con_coche.vehicle.models.Vehicle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.ArrayList;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class VehicleRentalOfferTest {
    @Test
    void builder_shouldCreateValidEntity() {
        Vehicle vehicle = new Vehicle();
        Location location = new Location();
        OwnerProfile owner = new OwnerProfile();

        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);

        VehicleRentalOffer offer = VehicleRentalOffer.builder()
                .id(1L)
                .vehicle(vehicle)
                .location(location)
                .owner(owner)
                .startDateTime(start)
                .endDateTime(end)
                .priceHour(25.5)
                .isAvailable(true)
                .slots(new ArrayList<>())
                .build();

        assertThat(offer).isNotNull();
        assertThat(offer.getId()).isEqualTo(1L);
        assertThat(offer.getVehicle()).isEqualTo(vehicle);
        assertThat(offer.getLocation()).isEqualTo(location);
        assertThat(offer.getOwner()).isEqualTo(owner);
        assertThat(offer.getStartDateTime()).isEqualTo(start);
        assertThat(offer.getEndDateTime()).isEqualTo(end);
        assertThat(offer.getPriceHour()).isEqualTo(25.5);
        assertThat(offer.isAvailable()).isTrue();
        assertThat(offer.getSlots()).isEmpty();
    }

    @Test
    void updateStatusIfNeeded_shouldSetUnavailableWhenExpired() {
        VehicleRentalOffer offer = VehicleRentalOffer.builder()
                .endDateTime(LocalDateTime.now().minusDays(1))
                .isAvailable(true)
                .build();

        offer.updateStatusIfNeeded();

        assertThat(offer.isAvailable()).isFalse();
    }

    @Test
    void settersAndGetters() {
        Vehicle vehicle = new Vehicle();
        Location location = new Location();
        OwnerProfile owner = new OwnerProfile();

        VehicleRentalOffer offer = new VehicleRentalOffer();
        offer.setId(5L);
        offer.setVehicle(vehicle);
        offer.setLocation(location);
        offer.setOwner(owner);
        offer.setStartDateTime(LocalDateTime.of(2025, 10, 5, 12, 0));
        offer.setEndDateTime(LocalDateTime.of(2025, 10, 6, 12, 0));
        offer.setPriceHour(30.0);
        offer.setAvailable(true);

        assertThat(offer.getId()).isEqualTo(5L);
        assertThat(offer.getVehicle()).isEqualTo(vehicle);
        assertThat(offer.getLocation()).isEqualTo(location);
        assertThat(offer.getOwner()).isEqualTo(owner);
        assertThat(offer.getStartDateTime()).isEqualTo(LocalDateTime.of(2025, 10, 5, 12, 0));
        assertThat(offer.getEndDateTime()).isEqualTo(LocalDateTime.of(2025, 10, 6, 12, 0));
        assertThat(offer.getPriceHour()).isEqualTo(30.0);
        assertThat(offer.isAvailable()).isTrue();
    }
}