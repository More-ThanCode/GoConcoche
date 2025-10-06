package com.more_than_code.go_con_coche.vehicle_reservation.services;

import com.more_than_code.go_con_coche.email.EmailService;
import com.more_than_code.go_con_coche.global.EntityNotFoundException;
import com.more_than_code.go_con_coche.global.UnauthorizedActionException;
import com.more_than_code.go_con_coche.owner_profile.OwnerProfile;
import com.more_than_code.go_con_coche.registered_user.RegisteredUser;
import com.more_than_code.go_con_coche.renter_profile.models.RenterProfile;
import com.more_than_code.go_con_coche.renter_profile.services.RenterProfileService;
import com.more_than_code.go_con_coche.vehicle.dtos.VehicleOfferResponse;
import com.more_than_code.go_con_coche.vehicle.models.Seater;
import com.more_than_code.go_con_coche.vehicle_rental_offer.models.RentalOfferSlot;
import com.more_than_code.go_con_coche.vehicle_rental_offer.models.VehicleRentalOffer;
import com.more_than_code.go_con_coche.vehicle_rental_offer.repositories.RentalOfferSlotRepository;
import com.more_than_code.go_con_coche.vehicle_rental_offer.repositories.VehicleRentalOfferRepository;
import com.more_than_code.go_con_coche.vehicle_rental_offer.services.RentalOfferSlotService;
import com.more_than_code.go_con_coche.vehicle_reservation.VehicleReservationRepository;
import com.more_than_code.go_con_coche.vehicle_reservation.dtos.VehicleReservationMapper;
import com.more_than_code.go_con_coche.vehicle_reservation.dtos.VehicleReservationRequest;
import com.more_than_code.go_con_coche.vehicle_reservation.dtos.VehicleReservationResponse;
import com.more_than_code.go_con_coche.vehicle_reservation.models.ReservationStatus;
import com.more_than_code.go_con_coche.vehicle_reservation.models.VehicleReservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VehicleReservationServiceImpl implements VehicleReservationService{

    private final VehicleReservationRepository reservationRepository;
    private final VehicleRentalOfferRepository offerRepository;
    private final RenterProfileService renterProfileService;
    private final RentalOfferSlotService slotService;
    private final RentalOfferSlotRepository slotRepository;
    private final VehicleReservationMapper reservationMapper;
    private final EmailService emailService;

    private int getVehicleCapacity (Seater seater) {
        return switch (seater) {
            case SMART -> 2;
            case SEDAN -> 4;
            case SUV -> 5;
            case VAN -> 7;
        };
    }

    private void applySlotReservationStrategy(
            RentalOfferSlot slot,
            VehicleRentalOffer offer) {

        slot.setAvailable(false);
        slotRepository.save(slot);
        boolean anyAvailable = offer.getSlots().stream().anyMatch(RentalOfferSlot::isAvailable);
        if (!anyAvailable) {
            offer.setAvailable(false);
            offerRepository.save(offer);
        }
    }

    private long calculateDurationInHours (LocalDateTime start, LocalDateTime end) {
        long minutes = ChronoUnit.MINUTES.between(start, end);
        return (long) Math.ceil(minutes / 60.0);
    }

    private String generateReservationCode() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
        return uuid.substring(0, 3) + "-" + uuid.substring(3, 6) + "-" + uuid.substring(6, 9);
    }

    @Override
    @Transactional
    public VehicleReservationResponse createReservation(VehicleReservationRequest request) {
        if(!request.endDateTime().isAfter(request.startDateTime())) {
            throw new IllegalArgumentException("End date-time must be after start date-time");
        }

        RenterProfile renterProfile = renterProfileService.getRenterProfileObj();

        VehicleRentalOffer offer = offerRepository.findById(request.offerId())
                .orElseThrow(() -> new EntityNotFoundException("VehicleRentalOffer", "id", String.valueOf(request.offerId())));
        if (request.startDateTime().isBefore(offer.getStartDateTime()) || request.endDateTime().isAfter(offer.getEndDateTime())) {
            throw new IllegalArgumentException("Reservation time must be fully within the offer period.");
        }

        int vehicleCapacity = getVehicleCapacity(offer.getVehicle().getSeater());
        if (request.travellerNumber() > vehicleCapacity) {
            throw new IllegalArgumentException(String.format("The number of travellers (%d) exceeds the vehicle's seating capacity (%d).", request.travellerNumber(), vehicleCapacity));
        }

        List<RentalOfferSlot> slotsToReserve = slotService.getSlotsWithinPeriod(
                request.offerId(), 
                request.startDateTime(),
                request.endDateTime()
        );
        
        if (slotsToReserve.isEmpty()) {
            throw new IllegalArgumentException("The requested reservation period does not overlap with any available slots.");
        }
        
        for (RentalOfferSlot slot : slotsToReserve) {
            if (slot.getSlotStart().isBefore(request.endDateTime()) && slot.getSlotEnd().isAfter(request.startDateTime())) {
                if (!slot.isAvailable()) {
                    throw new UnauthorizedActionException("make a reservation, time slot already reserved", "VehicleRentalOffer", offer.getId().toString());
                }
            }
        }

        for (RentalOfferSlot slot : slotsToReserve) {
            if (slot.getSlotStart().isBefore(request.endDateTime()) && slot.getSlotEnd().isAfter(request.startDateTime())) {
                applySlotReservationStrategy(slot, offer);
            }
        }

        long durationHours = calculateDurationInHours(request.startDateTime(), request.endDateTime());
        double totalPrice = durationHours * offer.getPriceHour();

        String reservationCode = generateReservationCode();
        VehicleReservation reservation = VehicleReservation.builder()
                .renter(renterProfile)
                .rentalOffer(offer)
                .reservationCode(reservationCode)
                .startDateTime(request.startDateTime())
                .endDateTime(request.endDateTime())
                .travellersNumber(request.travellerNumber())
                .totalPrice(totalPrice)
                .status(ReservationStatus.CONFIRMED)
                .build();

        VehicleReservation savedReservation = reservationRepository.save(reservation);

        VehicleOfferResponse vehicleDetails = reservationMapper.toResponse(savedReservation).vehicleDetails();

        emailService.sendConfirmationEmail(
                renterProfile.getRegisteredUser().getEmail(),
                renterProfile.getRegisteredUser().getUsername(),
                reservationCode,
                request.startDateTime(),
                request.endDateTime(),
                vehicleDetails.brand(),
                vehicleDetails.year(),
                vehicleDetails.color(),
                vehicleDetails.seats().toString(),
                vehicleDetails.childSeatsNumber() != null ? vehicleDetails.childSeatsNumber() : 0,
                vehicleDetails.model(),
                (int) durationHours,
                (int) offer.getPriceHour(),
                (int) totalPrice,
                offer.getLocation().getCity() + ", " + offer.getLocation().getDistrict(),
                vehicleDetails.imageUrl()
        );

        return reservationMapper.toResponse(savedReservation);
    }

    @Override
    public List<VehicleReservationResponse> getMyReservation() {
        RenterProfile renterProfile = renterProfileService.getRenterProfileObj();
        List<VehicleReservation> reservations = reservationRepository.findByRenterId(renterProfile.getId());
        return reservations.stream().map(reservationMapper::toResponse).toList();
    }
}