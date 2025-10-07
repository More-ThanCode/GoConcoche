package com.more_than_code.go_con_coche.vehicle.dtos;

import com.more_than_code.go_con_coche.vehicle.models.FuelTypeCar;
import com.more_than_code.go_con_coche.vehicle.models.Seater;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.mail.Multipart;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;
@Schema(description = "Request to create a vehicle")
@Builder
public record VehicleRequest (
        @NotBlank
        @Schema(description = "Vehicle Identification Number", example = "1HGCM82633A004352")
        String vin,

        @NotBlank
        @Schema(description = "License plate number", example = "1234ABC")
        String plateNumber,

        @NotBlank
        @Schema(description = "Insurance policy number", example = "INS-987654322")
        String insuranceNumber,

        @NotBlank
        @Schema(description = "Vehicle model", example = "Renegade")
        String model,

        @NotBlank
        @Schema(description = "Vehicle brand", example = "Jeep")
        String brand,

        @NotNull
        @Schema(description = "Manufacture year", example = "2023")
        Integer year,

        @NotBlank
        @Schema(description = "Vehicle color", example = "White")
        String color,

        @NotNull
        @Schema(description = "Number of seats", example = "5")
        Seater seater,

        @Schema(description = "Number of child seats", example = "2")
        Integer childSeatsNumber,

        @NotNull
        @Schema(description = "Fuel type of the car", example = "DIESEL")
        FuelTypeCar fuelTypeCar,

        @NotBlank
        @Schema(description = "Fuel consumption per 100 km", example = "5.9 L/100km")
        String fuelConsumption,

        @Schema(description = "Vehicle image file")
        MultipartFile image
) {
}