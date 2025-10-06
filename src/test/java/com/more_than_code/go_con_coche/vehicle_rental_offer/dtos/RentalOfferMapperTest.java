package com.more_than_code.go_con_coche.vehicle_rental_offer.dtos;

import com.more_than_code.go_con_coche.location.Location;
import com.more_than_code.go_con_coche.location.dtos.LocationMapper;
import com.more_than_code.go_con_coche.vehicle.dtos.VehicleMapper;
import com.more_than_code.go_con_coche.vehicle.dtos.VehicleOfferResponse;
import com.more_than_code.go_con_coche.vehicle.models.FuelTypeCar;
import com.more_than_code.go_con_coche.vehicle.models.Seater;
import com.more_than_code.go_con_coche.vehicle.models.Vehicle;
import com.more_than_code.go_con_coche.vehicle_rental_offer.models.VehicleRentalOffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentalOfferMapperTest {
    @Mock
    private VehicleMapper vehicleMapper;

    @Mock
    private LocationMapper locationMapper;

    private RentalOfferMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new RentalOfferMapper(vehicleMapper, locationMapper);
    }

    @Test
    void toEntity_shouldMapCorrectly() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);
        double priceHour = 20.0;

        RentalOfferRequest request = new RentalOfferRequest(1L, 2L, start, end, priceHour);

        VehicleRentalOffer result = mapper.toEntity(request);

        assertThat(result).isNotNull();
        assertThat(result.getStartDateTime()).isEqualTo(start);
        assertThat(result.getEndDateTime()).isEqualTo(end);
        assertThat(result.getPriceHour()).isEqualTo(priceHour);
    }

    @Test
    void toRentalOfferResponse_shouldMapCorrectly() {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(1L);
        vehicle.setModel("Model 3");

        Location location = new Location();
        location.setId(10L);
        location.setCity("Kyiv");

        VehicleRentalOffer entity = VehicleRentalOffer.builder()
                .id(100L)
                .vehicle(vehicle)
                .location(location)
                .startDateTime(LocalDateTime.of(2025, 10, 1, 10, 0))
                .endDateTime(LocalDateTime.of(2025, 10, 2, 10, 0))
                .priceHour(20.0)
                .build();

        VehicleOfferResponse vehicleResponse = VehicleOfferResponse.builder()
                .id(1L)
                .model("Model 3")
                .brand("Tesla")
                .year(2024)
                .color("White")
                .seats(Seater.SEDAN)
                .childSeatsNumber(1)
                .fuelTypeCar(FuelTypeCar.ELECTRIC)
                .fuelConsumption("15 kWh/100km")
                .imageUrl("https://example.com/tesla.jpg")
                .build();
        when(vehicleMapper.toVehicleOfferResponse(vehicle)).thenReturn(vehicleResponse);

        RentalOfferResponse response = mapper.toRentalOfferResponse(entity);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(100L);
        assertThat(response.vehicle().model()).isEqualTo("Model 3");
        assertThat(response.location()).isEqualTo("Kyiv");
        assertThat(response.priceHour()).isEqualTo(20.0);

        verify(vehicleMapper).toVehicleOfferResponse(vehicle);
        verifyNoInteractions(locationMapper);
    }
}