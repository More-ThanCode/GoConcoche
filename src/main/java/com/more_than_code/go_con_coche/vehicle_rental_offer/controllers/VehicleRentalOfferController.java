package com.more_than_code.go_con_coche.vehicle_rental_offer.controllers;

import com.more_than_code.go_con_coche.vehicle_rental_offer.dtos.RentalOfferRequest;
import com.more_than_code.go_con_coche.vehicle_rental_offer.dtos.RentalOfferResponse;
import com.more_than_code.go_con_coche.vehicle_rental_offer.services.VehicleRentalOfferServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicle-rental-offers")
@RequiredArgsConstructor
@Tag(name ="Rental Offers", description = "Operations to create, retrieve, and delete rental offers.")
public class VehicleRentalOfferController {
    private final VehicleRentalOfferServiceImpl rentalOfferService;

    @Operation(summary = "Create a rental offer", description = "Creates a new rental offer for a vehicle")
    @PostMapping("")
    public ResponseEntity<RentalOfferResponse> createRentalOffer(@Valid @RequestBody RentalOfferRequest rentalOfferRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(rentalOfferService.createRenterOffer(rentalOfferRequest));
    }

    @Operation(summary = "Get my rental offers", description = "Returns all rental offers created by the authenticated owner")
    @GetMapping("/my-offers")
    public ResponseEntity<List<RentalOfferResponse>> getMyRentalOffers(){
        return ResponseEntity.ok()
                .body(rentalOfferService.getMyRentalOffers());
    }

    @Operation(summary = "Delete a rental offer", description = "Deletes a rental offer by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRentalOffer(@PathVariable Long id){
        rentalOfferService.deleteRentalOffer(id);
        return ResponseEntity.noContent().build();
    }
}