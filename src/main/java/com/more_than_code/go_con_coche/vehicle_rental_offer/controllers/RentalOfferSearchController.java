package com.more_than_code.go_con_coche.vehicle_rental_offer.controllers;

import com.more_than_code.go_con_coche.vehicle_rental_offer.dtos.search.SearchCriteria;
import com.more_than_code.go_con_coche.vehicle_rental_offer.dtos.search.SearchOfferRequest;
import com.more_than_code.go_con_coche.vehicle_rental_offer.dtos.search.SearchOfferResponse;
import com.more_than_code.go_con_coche.vehicle_rental_offer.services.RentalOfferSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/vehicle-rental-offers")
@RequiredArgsConstructor
@Tag(name = "Rental Offers", description = "Operations to search and retrieve rental offers")
public class RentalOfferSearchController {
    private final RentalOfferSearchService searchService;

    @Operation(summary = "Search offers by criteria", description = "Search rental offers filtered by city, date range, vehicle model, and minimum seats.")
    @GetMapping("/search/by-criteria")
    public ResponseEntity<List<SearchOfferResponse>> searchOffersByCriteria(
            @Parameter(description = "City name (case insensitive, partial match)")
            @RequestParam(required = false)
            String city,

            @Parameter(description = "Start date and time")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime startDateTime,

            @Parameter(description = "End date and time")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime endDateTime,

            @Parameter(description = "Vehicle model name (case insensitive, partial match)")
            @RequestParam(required = false)
            String model,

            @Parameter(description = "Minimum number of seats required")
            @RequestParam(required = false)
            Integer seats
    ) {
        SearchCriteria criteria = SearchCriteria.builder()
                .city(city)
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .model(model)
                .seats(seats)
                .build();

        List<SearchOfferResponse> offers = searchService.searchWithCriteria(criteria);
        return ResponseEntity.ok(offers);
    }
}