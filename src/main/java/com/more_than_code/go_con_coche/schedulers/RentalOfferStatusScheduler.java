package com.more_than_code.go_con_coche.schedulers;

import com.more_than_code.go_con_coche.vehicle_rental_offer.models.VehicleRentalOffer;
import com.more_than_code.go_con_coche.vehicle_rental_offer.repositories.VehicleRentalOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@EnableScheduling
@Component
@RequiredArgsConstructor
public class RentalOfferStatusScheduler {
    private final VehicleRentalOfferRepository offerRepository;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void updateRentalOfferStatuses() {
        LocalDateTime now = LocalDateTime.now();
        List<VehicleRentalOffer> rentalOffers = offerRepository.findByEndDateTimeBeforeAndIsAvailableTrue(now);
        rentalOffers.forEach(offer -> offer.setAvailable(false));
        offerRepository.saveAll(rentalOffers);
    }
}
