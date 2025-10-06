package com.more_than_code.go_con_coche.vehicle.dtos;

import com.more_than_code.go_con_coche.owner_profile.OwnerProfile;
import com.more_than_code.go_con_coche.registered_user.RegisteredUser;
import com.more_than_code.go_con_coche.vehicle.models.FuelTypeCar;
import com.more_than_code.go_con_coche.vehicle.models.Seater;
import com.more_than_code.go_con_coche.vehicle.models.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VehicleMapperTest {

    private VehicleMapper vehicleMapper;

    private Vehicle vehicle;
    private VehicleRequest vehicleRequest;

    @BeforeEach
    void setUp() {
        vehicleMapper = new VehicleMapper();

        RegisteredUser user = new RegisteredUser();
        user.setId(1L);
        user.setUsername("testUser");

        OwnerProfile owner = new OwnerProfile();
        owner.setId(1L);
        owner.setRegisteredUser(user);

        vehicleRequest = VehicleRequest.builder()
                .vin("VIN123")
                .plateNumber("ABC123")
                .insuranceNumber("INS123")
                .model("Civic")
                .brand("Honda")
                .year(2022)
                .color("Red")
                .seater(Seater.SEDAN)
                .childSeatsNumber(2)
                .fuelTypeCar(FuelTypeCar.DIESEL)
                .fuelConsumption("6.5L/100km")
                .image(null)
                .build();

        vehicle = Vehicle.builder()
                .id(1L)
                .vin("VIN123")
                .plateNumber("ABC123")
                .insuranceNumber("INS123")
                .model("Civic")
                .brand("Honda")
                .year(2022)
                .color("Red")
                .seater(Seater.SEDAN)
                .childSeatsNumber(2)
                .fuelTypeCar(FuelTypeCar.DIESEL)
                .fuelConsumption("6.5L/100km")
                .imageUrl("http://image.com/car.jpg")
                .owner(owner)
                .build();
    }

    @Test
    void toEntity_ShouldMapAllFields() {
        Vehicle result = vehicleMapper.toEntity(vehicleRequest);

        assertEquals(vehicleRequest.vin(), result.getVin());
        assertEquals(vehicleRequest.plateNumber(), result.getPlateNumber());
        assertEquals(vehicleRequest.insuranceNumber(), result.getInsuranceNumber());
        assertEquals(vehicleRequest.model(), result.getModel());
        assertEquals(vehicleRequest.brand(), result.getBrand());
        assertEquals(vehicleRequest.year(), result.getYear());
        assertEquals(vehicleRequest.color(), result.getColor());
        assertEquals(vehicleRequest.seater(), result.getSeater());
        assertEquals(vehicleRequest.childSeatsNumber(), result.getChildSeatsNumber());
        assertEquals(vehicleRequest.fuelTypeCar(), result.getFuelTypeCar());
        assertEquals(vehicleRequest.fuelConsumption(), result.getFuelConsumption());
    }

    @Test
    void toResponse_ShouldMapAllFields() {
        var result = vehicleMapper.toResponse(vehicle);

        assertEquals(vehicle.getId(), result.id());
        assertEquals(vehicle.getVin(), result.vin());
        assertEquals(vehicle.getPlateNumber(), result.plateNumber());
        assertEquals(vehicle.getInsuranceNumber(), result.insuranceNumber());
        assertEquals(vehicle.getModel(), result.model());
        assertEquals(vehicle.getBrand(), result.brand());
        assertEquals(vehicle.getYear(), result.year());
        assertEquals(vehicle.getColor(), result.color());
        assertEquals(vehicle.getSeater(), result.seater());
        assertEquals(vehicle.getChildSeatsNumber(), result.childSeatsNumber());
        assertEquals(vehicle.getFuelTypeCar(), result.fuelTypeCar());
        assertEquals(vehicle.getFuelConsumption(), result.fuelConsumption());
        assertEquals(vehicle.getImageUrl(), result.imageUrl());
        assertEquals(vehicle.getOwner().getRegisteredUser().getUsername(), result.username());
    }

    @Test
    void updateFromDto_ShouldUpdateOnlyNonNullFields() {
        VehicleRequest updateRequest = VehicleRequest.builder()
                .vin("VIN999")  // cambio de VIN
                .model(null)    // no se debe actualizar
                .color("Blue")  // cambio de color
                .build();

        vehicleMapper.updateFromDto(updateRequest, vehicle);

        assertEquals("VIN999", vehicle.getVin());
        assertEquals("Civic", vehicle.getModel()); // sigue igual
        assertEquals("Blue", vehicle.getColor());
    }

    @Test
    void toVehicleOfferResponse_ShouldMapCorrectly() {
        var result = vehicleMapper.toVehicleOfferResponse(vehicle);

        assertEquals(vehicle.getId(), result.id());
        assertEquals(vehicle.getModel(), result.model());
        assertEquals(vehicle.getBrand(), result.brand());
        assertEquals(vehicle.getYear(), result.year());
        assertEquals(vehicle.getColor(), result.color());
        assertEquals(vehicle.getSeater(), result.seats());
        assertEquals(vehicle.getChildSeatsNumber(), result.childSeatsNumber());
        assertEquals(vehicle.getFuelTypeCar(), result.fuelTypeCar());
        assertEquals(vehicle.getFuelConsumption(), result.fuelConsumption());
        assertEquals(vehicle.getImageUrl(), result.imageUrl());
    }
}

