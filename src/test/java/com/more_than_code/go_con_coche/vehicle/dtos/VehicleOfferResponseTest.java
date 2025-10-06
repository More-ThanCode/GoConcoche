package com.more_than_code.go_con_coche.vehicle.dtos;

import com.more_than_code.go_con_coche.vehicle.models.FuelTypeCar;
import com.more_than_code.go_con_coche.vehicle.models.Seater;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VehicleOfferResponseTest {

    @Test
    void vehicleOfferResponse_ShouldCreateCorrectInstance() {
        VehicleOfferResponse response = VehicleOfferResponse.builder()
                .id(1L)
                .model("Civic")
                .brand("Honda")
                .year(2022)
                .color("Red")
                .seats(Seater.SEDAN)
                .childSeatsNumber(2)
                .fuelTypeCar(FuelTypeCar.DIESEL)
                .fuelConsumption("6.5L/100km")
                .imageUrl("http://image.com/car.jpg")
                .build();

        assertEquals(1L, response.id());
        assertEquals("Civic", response.model());
        assertEquals("Honda", response.brand());
        assertEquals(2022, response.year());
        assertEquals("Red", response.color());
        assertEquals(Seater.SEDAN, response.seats());
        assertEquals(2, response.childSeatsNumber());
        assertEquals(FuelTypeCar.DIESEL, response.fuelTypeCar());
        assertEquals("6.5L/100km", response.fuelConsumption());
        assertEquals("http://image.com/car.jpg", response.imageUrl());
    }

    @Test
    void vehicleOfferResponse_EqualsAndHashCode_ShouldWorkCorrectly() {
        VehicleOfferResponse r1 = VehicleOfferResponse.builder()
                .id(1L)
                .model("Civic")
                .brand("Honda")
                .year(2022)
                .color("Red")
                .seats(Seater.SEDAN)
                .childSeatsNumber(2)
                .fuelTypeCar(FuelTypeCar.DIESEL)
                .fuelConsumption("6.5L/100km")
                .imageUrl("http://image.com/car.jpg")
                .build();

        VehicleOfferResponse r2 = VehicleOfferResponse.builder()
                .id(1L)
                .model("Civic")
                .brand("Honda")
                .year(2022)
                .color("Red")
                .seats(Seater.SEDAN)
                .childSeatsNumber(2)
                .fuelTypeCar(FuelTypeCar.DIESEL)
                .fuelConsumption("6.5L/100km")
                .imageUrl("http://image.com/car.jpg")
                .build();

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }
}

