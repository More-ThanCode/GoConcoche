package com.more_than_code.go_con_coche.vehicle_reservation;

import com.more_than_code.go_con_coche.vehicle_reservation.dtos.VehicleReservationRequest;
import com.more_than_code.go_con_coche.vehicle_reservation.dtos.VehicleReservationResponse;
import com.more_than_code.go_con_coche.vehicle_reservation.services.VehicleReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Vehicle Reservations",description = "Operations related to creating and viewing vehicle rental reservations")
@RestController
@RequestMapping("/api/vehicle-reservations")
@RequiredArgsConstructor
public class VehicleReservationController {

    private final VehicleReservationService reservationService;

    @PostMapping
    @PreAuthorize("hasRole('RENTER')")
    @Operation(summary = "Create a new vehicle reservation", description = " Creates a reservation for a selected rental offer within the offer's available time slots.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reservation successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid request data or unavailable slot"),
            @ApiResponse(responseCode = "401", description = "Unauthorized — renter not logged in"),
            @ApiResponse(responseCode = "403", description = "Access denied for current user role")
    })
    public ResponseEntity<VehicleReservationResponse> createReservation(@Valid @RequestBody VehicleReservationRequest request) {
        VehicleReservationResponse response = reservationService.createReservation(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/my-reservations")
    @PreAuthorize("hasRole('RENTER')")
    @Operation(summary = "Get all my reservations", description = "Retrieves all vehicle reservations made by the currently logged-in renter.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reservations successfully retrieved"),
            @ApiResponse(responseCode = "204", description = "No reservations found for this renter"),
            @ApiResponse(responseCode = "401", description = "Unauthorized — renter not logged in")
    })
    public ResponseEntity<List<VehicleReservationResponse>> getMyReservations() {
        List<VehicleReservationResponse> response = reservationService.getMyReservation();
        return ResponseEntity.ok(response);
    }
}