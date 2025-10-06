package com.more_than_code.go_con_coche.vehicle_rental_offer.services;

import com.more_than_code.go_con_coche.vehicle_rental_offer.VehicleRentalOfferSpecification;
import com.more_than_code.go_con_coche.vehicle_rental_offer.dtos.search.SearchCriteria;
import com.more_than_code.go_con_coche.vehicle_rental_offer.dtos.search.SearchOfferResponse;
import com.more_than_code.go_con_coche.vehicle_rental_offer.models.RentalOfferSlot;
import com.more_than_code.go_con_coche.vehicle_rental_offer.models.VehicleRentalOffer;
import com.more_than_code.go_con_coche.vehicle_rental_offer.repositories.VehicleRentalOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RentalOfferSearchService {
    private final VehicleRentalOfferRepository offerRepository;
    private final RentalOfferSlotService slotService;

    @Transactional(readOnly = true)
    public List<SearchOfferResponse> searchWithCriteria(SearchCriteria criteria) {
        Specification<VehicleRentalOffer> specification = VehicleRentalOfferSpecification.withCriteria(criteria);
        List<VehicleRentalOffer> offers = offerRepository.findAll(specification);

        List<SearchOfferResponse> result = new ArrayList<>();

        for (VehicleRentalOffer offer : offers) {

        if (criteria.getStartDateTime() != null && criteria.getEndDateTime() != null) {
            List<RentalOfferSlot> availableSlots = slotService.getSlotsWithinPeriod(
                            offer.getId(),
                            criteria.getStartDateTime(),
                            criteria.getEndDateTime()
                    )
                    .stream()
                    .filter(RentalOfferSlot::isAvailable)
                    .toList();

            if (availableSlots.isEmpty()) {
                continue;
            }
        }

        result.add(new SearchOfferResponse(
                offer.getId(),
                offer.getVehicle().getModel(),
                offer.getVehicle().getBrand(),
                offer.getLocation().getCity(),
                offer.getStartDateTime(),
                offer.getEndDateTime(),
                offer.getPriceHour()
        ));
        }
        return result;
    }
}