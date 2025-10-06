package com.more_than_code.go_con_coche.vehicle_rental_offer.dtos.search;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record SearchOfferResponse(
        Long offerId,
        String vehicleModel,
        String vehicleBrand,
        String city,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        double priceHour
) {}