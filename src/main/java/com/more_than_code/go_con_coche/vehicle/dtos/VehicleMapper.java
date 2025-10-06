package com.more_than_code.go_con_coche.vehicle.dtos;

import com.more_than_code.go_con_coche.vehicle.models.Vehicle;
import org.springframework.stereotype.Component;

@Component
public class VehicleMapper {

    public Vehicle toEntity(VehicleRequest vehicleRequest) {
        return Vehicle.builder()
                .vin(vehicleRequest.vin())
                .plateNumber(vehicleRequest.plateNumber())
                .insuranceNumber(vehicleRequest.insuranceNumber())
                .model(vehicleRequest.model())
                .brand(vehicleRequest.brand())
                .year(vehicleRequest.year())
                .color(vehicleRequest.color())
                .seater(vehicleRequest.seater())
                .childSeatsNumber(vehicleRequest.childSeatsNumber())
                .fuelTypeCar(vehicleRequest.fuelTypeCar())
                .fuelConsumption(vehicleRequest.fuelConsumption())
                .build();
    }

    public VehicleResponse toResponse(Vehicle vehicle) {
        return VehicleResponse.builder()
                .id(vehicle.getId())
                .vin(vehicle.getVin())
                .plateNumber(vehicle.getPlateNumber())
                .insuranceNumber(vehicle.getInsuranceNumber())
                .model(vehicle.getModel())
                .brand(vehicle.getBrand())
                .year(vehicle.getYear())
                .color(vehicle.getColor())
                .seater(vehicle.getSeater())
                .childSeatsNumber(vehicle.getChildSeatsNumber())
                .fuelTypeCar(vehicle.getFuelTypeCar())
                .fuelConsumption(vehicle.getFuelConsumption())
                .imageUrl(vehicle.getImageUrl())
                .username(vehicle.getOwner().getRegisteredUser().getUsername())
                .build();
    }

    public void updateFromDto(VehicleRequest request, Vehicle vehicle) {
        if (request.vin() != null) {
            vehicle.setVin(request.vin());
        }
        if (request.plateNumber() != null) {
            vehicle.setPlateNumber(request.plateNumber());
        }
        if (request.insuranceNumber() != null) {
            vehicle.setInsuranceNumber(request.insuranceNumber());
        }
        if (request.model() != null) {
            vehicle.setModel(request.model());
        }
        if (request.brand() != null) {
            vehicle.setBrand(request.brand());
        }
        if (request.year() != null) {
            vehicle.setYear(request.year());
        }
        if (request.color() != null) {
            vehicle.setColor(request.color());
        }
        if (request.seater() != null) {
            vehicle.setSeater(request.seater());
        }
        if (request.childSeatsNumber() != null) {
            vehicle.setChildSeatsNumber(request.childSeatsNumber());
        }
        if (request.fuelTypeCar() != null) {
            vehicle.setFuelTypeCar(request.fuelTypeCar());
        }
        if (request.fuelConsumption() != null) {
            vehicle.setFuelConsumption(request.fuelConsumption());
        }
    }

    public VehicleOfferResponse toVehicleOfferResponse(Vehicle vehicle) {
        return VehicleOfferResponse.builder()
                .id(vehicle.getId())
                .model(vehicle.getModel())
                .brand(vehicle.getBrand())
                .year(vehicle.getYear())
                .color(vehicle.getColor())
                .seats(vehicle.getSeater())
                .childSeatsNumber(vehicle.getChildSeatsNumber())
                .fuelTypeCar(vehicle.getFuelTypeCar())
                .fuelConsumption(vehicle.getFuelConsumption())
                .imageUrl(vehicle.getImageUrl())
                .build();
    }
}