package com.more_than_code.go_con_coche.vehicle;

import com.more_than_code.go_con_coche.vehicle.dtos.VehicleRequest;
import com.more_than_code.go_con_coche.vehicle.dtos.VehicleResponse;
import com.more_than_code.go_con_coche.vehicle.services.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
@Tag(name = "Vehicles", description = "Operations related to vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    @Operation(summary = "Create a new vehicle", description = "Adds a vehicle to the authenticated owner's profile")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<VehicleResponse> createVehicle(@Valid @ModelAttribute VehicleRequest vehicleRequest) {
        VehicleResponse createdVehicle = vehicleService.createVehicle(vehicleRequest);
        return new ResponseEntity<>(createdVehicle, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all vehicles", description = "Returns a list of all vehicles")
    @GetMapping
    public ResponseEntity<List<VehicleResponse>> getAllVehicles() {
        List<VehicleResponse> vehicles = vehicleService.getAllVehicles();
        return new ResponseEntity<>(vehicles, HttpStatus.OK);
    }

    @Operation(summary = "Get vehicles by owner ID", description = "Returns a list of vehicles for a specific owner")
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<VehicleResponse>> getVehiclesByOwnerId(@PathVariable Long ownerId) {
        List<VehicleResponse> vehicles = vehicleService.getVehicleByOwnerId(ownerId);
        return new ResponseEntity<>(vehicles, HttpStatus.OK);
    }

    @Operation(summary = "Get my vehicles", description = "Returns the vehicles of the authenticated owner")
    @GetMapping("/my")
    public ResponseEntity<List<VehicleResponse>> getMyVehicles() {
        List<VehicleResponse> vehicles = vehicleService.getMyVehicles();
        return new ResponseEntity<>(vehicles, HttpStatus.OK);
    }

    @Operation(summary = "Update a vehicle", description = "Updates a vehicle by ID")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PutMapping("/{id}")
    public ResponseEntity<VehicleResponse> updateVehicle(@PathVariable Long id, @Valid @ModelAttribute VehicleRequest vehicleRequest) {
        VehicleResponse updatedVehicle = vehicleService.updateVehicle(id, vehicleRequest);
        return new ResponseEntity<>(updatedVehicle, HttpStatus.OK);
    }

    @Operation(summary = "Delete a vehicle", description = "Deletes a vehicle by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }
}