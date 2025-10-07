package com.more_than_code.go_con_coche.vehicle_rental_offer.services;

import com.more_than_code.go_con_coche.global.EntityNotFoundException;
import com.more_than_code.go_con_coche.location.Location;
import com.more_than_code.go_con_coche.location.services.LocationServiceImpl;
import com.more_than_code.go_con_coche.owner_profile.OwnerProfile;
import com.more_than_code.go_con_coche.owner_profile.service.OwnerProfileService;
import com.more_than_code.go_con_coche.registered_user.RegisteredUser;
import com.more_than_code.go_con_coche.registered_user.services.UserAuthService;
import com.more_than_code.go_con_coche.vehicle.models.Vehicle;
import com.more_than_code.go_con_coche.vehicle.services.VehicleServiceImpl;
import com.more_than_code.go_con_coche.vehicle_rental_offer.dtos.RentalOfferMapper;
import com.more_than_code.go_con_coche.vehicle_rental_offer.dtos.RentalOfferRequest;
import com.more_than_code.go_con_coche.vehicle_rental_offer.dtos.RentalOfferResponse;
import com.more_than_code.go_con_coche.vehicle_rental_offer.models.RentalOfferSlot;
import com.more_than_code.go_con_coche.vehicle_rental_offer.models.VehicleRentalOffer;
import com.more_than_code.go_con_coche.vehicle_rental_offer.repositories.VehicleRentalOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class VehicleRentalOfferServiceImpl implements VehicleRentalOfferService {
    private final VehicleRentalOfferRepository offerRepository;
    private final RentalOfferMapper rentalOfferMapper;
    private final VehicleServiceImpl vehicleService;
    private final LocationServiceImpl locationService;
    private final OwnerProfileService ownerProfileService;
    private final UserAuthService userService;

    @Override
    public RentalOfferResponse createRenterOffer(RentalOfferRequest rentalOfferRequest) {
        if (!rentalOfferRequest.endDateTime().isAfter(rentalOfferRequest.startDateTime())) {
            throw new IllegalArgumentException("End date-time must be after start date-time");
        }

        OwnerProfile ownerProfile = ownerProfileService.getOwnerProfileObj();
        Vehicle vehicle = vehicleService.getVehicleByIdObj(rentalOfferRequest.vehicleId());

        if(vehicle.getOwner().getId() != ownerProfile.getId()) {
            throw new AccessDeniedException("You are not allowed to modify this vehicle rental offer");
        }

        Location location = locationService.getLocationByIdObj(rentalOfferRequest.locationId());

        if (offerRepository.existsOverlappingOffer(vehicle.getId(), rentalOfferRequest.startDateTime(), rentalOfferRequest.endDateTime())) {
            throw new IllegalArgumentException("Vehicle already has an overlapping offer for the selected dates");
        }

        VehicleRentalOffer rentalOffer = rentalOfferMapper.toEntity(rentalOfferRequest);
        rentalOffer.setVehicle(vehicle);
        rentalOffer.setLocation(location);
        rentalOffer.setOwner(ownerProfile);
        rentalOffer.setAvailable(true);
        rentalOffer.setSlots(generateSlots(rentalOffer));
        VehicleRentalOffer savedOffer = offerRepository.save(rentalOffer);
        return rentalOfferMapper.toRentalOfferResponse(savedOffer);
    }

    @Override
    public List<RentalOfferResponse> getMyRentalOffers() {
        RegisteredUser owner = userService.getAuthenticatedUser();
        OwnerProfile ownerProfile = ownerProfileService.getOwnerProfileObj();
        List<VehicleRentalOffer> offers = offerRepository.findByOwnerId(ownerProfile.getId());
        return offers.stream().map(rentalOfferMapper::toRentalOfferResponse).toList();
    }

    @Override
    public void deleteRentalOffer(Long id) {
        RegisteredUser owner = userService.getAuthenticatedUser();
        OwnerProfile ownerProfile = ownerProfileService.getOwnerProfileObj();
        VehicleRentalOffer offer = offerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("VehicleRentalOffer", "id", String.valueOf(owner.getId())));
        if(ownerProfile.getId() != offer.getOwner().getId()) {
            throw new AccessDeniedException("You are not allowed to modify this vehicle rental offer");
        }
        offerRepository.deleteById(id);
    }

    private List<RentalOfferSlot> generateSlots(VehicleRentalOffer offer) {
        List<RentalOfferSlot> slots = new ArrayList<>();

        LocalDateTime current = offer.getStartDateTime();
        while (current.isBefore(offer.getEndDateTime())) {
            LocalDateTime slotEnd = current.plusHours(8);
            if (slotEnd.isAfter(offer.getEndDateTime())) {
                slotEnd = offer.getEndDateTime();
            }
            RentalOfferSlot slot = RentalOfferSlot.builder()
                    .offer(offer)
                    .slotStart(current)
                    .slotEnd(slotEnd)
                    .isAvailable(true)
                    .build();
            slots.add(slot);
            current = slotEnd;
        }
        return slots;
    }
}