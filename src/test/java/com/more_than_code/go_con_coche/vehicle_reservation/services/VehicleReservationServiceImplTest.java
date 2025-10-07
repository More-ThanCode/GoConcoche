package com.more_than_code.go_con_coche.vehicle_reservation.services;

import com.more_than_code.go_con_coche.email.EmailService;
import com.more_than_code.go_con_coche.location.Location;
import com.more_than_code.go_con_coche.registered_user.RegisteredUser;
import com.more_than_code.go_con_coche.renter_profile.models.RenterProfile;
import com.more_than_code.go_con_coche.renter_profile.models.TypeLicense;
import com.more_than_code.go_con_coche.renter_profile.services.RenterProfileService;
import com.more_than_code.go_con_coche.vehicle.dtos.VehicleOfferResponse;
import com.more_than_code.go_con_coche.vehicle.models.Seater;
import com.more_than_code.go_con_coche.vehicle.models.Vehicle;
import com.more_than_code.go_con_coche.vehicle_rental_offer.models.RentalOfferSlot;
import com.more_than_code.go_con_coche.vehicle_rental_offer.models.VehicleRentalOffer;
import com.more_than_code.go_con_coche.vehicle_rental_offer.repositories.RentalOfferSlotRepository;
import com.more_than_code.go_con_coche.vehicle_rental_offer.repositories.VehicleRentalOfferRepository;
import com.more_than_code.go_con_coche.vehicle_rental_offer.services.RentalOfferSlotService;
import com.more_than_code.go_con_coche.vehicle_reservation.VehicleReservationRepository;
import com.more_than_code.go_con_coche.vehicle_reservation.dtos.VehicleReservationMapper;
import com.more_than_code.go_con_coche.vehicle_reservation.dtos.VehicleReservationRequest;
import com.more_than_code.go_con_coche.vehicle_reservation.dtos.VehicleReservationResponse;
import com.more_than_code.go_con_coche.vehicle_reservation.models.VehicleReservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleReservationServiceImplTest {
    @Mock
    private VehicleReservationRepository reservationRepository;

    @Mock
    private VehicleRentalOfferRepository offerRepository;

    @Mock
    private RenterProfileService renterProfileService;

    @Mock
    private RentalOfferSlotService slotService;

    @Mock
    private RentalOfferSlotRepository slotRepository;

    @Mock
    private VehicleReservationMapper reservationMapper;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private VehicleReservationServiceImpl reservationService;

    private VehicleRentalOffer offer;
    private VehicleReservationRequest request;
    private RenterProfile renter;
    private RentalOfferSlot slot;
    private Vehicle vehicle;

    @BeforeEach
    void setUp() {
        RegisteredUser user = new RegisteredUser();
        user.setId(1L);
        user.setEmail("test@mail.com");
        user.setUsername("testUser");

        renter = new RenterProfile();
        renter.setId(1L);
        renter.setRegisteredUser(user);

        vehicle = new Vehicle();
        vehicle.setSeater(Seater.SEDAN);

        offer = new VehicleRentalOffer();
        offer.setId(10L);
        offer.setVehicle(vehicle);
        offer.setAvailable(true);
        offer.setStartDateTime(LocalDateTime.of(2025, 10, 1, 10, 0));
        offer.setEndDateTime(LocalDateTime.of(2025, 10, 10, 18, 0));
        offer.setPriceHour(20.0);
        offer.setSlots(new ArrayList<>());

        Location location = new Location();
        location.setCity("Kyiv");
        location.setDistrict("Rusanivka");
        offer.setLocation(location);

        slot = new RentalOfferSlot();
        slot.setAvailable(true);
        slot.setSlotStart(LocalDateTime.of(2025, 10, 2, 10, 0));
        slot.setSlotEnd(LocalDateTime.of(2025, 10, 2, 18, 0));

        request = new VehicleReservationRequest(
                10L,
                LocalDateTime.of(2025, 10, 2, 10, 0),
                LocalDateTime.of(2025, 10, 2, 18, 0),
                2);
    }

    @Test
    void createReservation_success() {
        renter.setTypeLicense(TypeLicense.B);
        when(renterProfileService.getRenterProfileObj()).thenReturn(renter);
        when(offerRepository.findById(10L)).thenReturn(Optional.of(offer));
        when(slotService.getSlotsWithinPeriod(anyLong(), any(), any())).thenReturn(List.of(slot));
        when(reservationRepository.save(any())).thenAnswer(invocation -> {
            VehicleReservation reservation = invocation.getArgument(0);
            reservation.setId(99L);
            return reservation;});

        VehicleOfferResponse vehicleOfferResponse = VehicleOfferResponse.builder()
                .brand("Toyota")
                .model("Corolla")
                .year(2022)
                .color("Blue")
                .seats(Seater.SEDAN)
                .childSeatsNumber(1)
                .imageUrl("img.png")
                .build();

        VehicleReservationResponse mappedResponse = VehicleReservationResponse.builder()
                .id(99L)
                .reservationCode("ABC-DEF-GHI")
                .vehicleDetails(vehicleOfferResponse)
                .build();

        when(reservationMapper.toResponse(any())).thenReturn(mappedResponse);

        VehicleReservationResponse response = reservationService.createReservation(request);

        assertNotNull(response);
        verify(slotRepository).save(slot);
        verify(offerRepository).save(offer);
    }

    @Test
    void createReservation_whenEndBeforeStart() {
        VehicleReservationRequest invalidRequest = new VehicleReservationRequest(
                10L,
                LocalDateTime.of(2025, 10, 2, 18, 0),
                LocalDateTime.of(2025, 10, 2, 10, 0),
                2);
        assertThrows(IllegalArgumentException.class,
                () -> reservationService.createReservation(invalidRequest));
    }

    @Test
    void createReservation_whenTooManyTravellers() {
        offer.getVehicle().setSeater(Seater.SMART);
        renter.setTypeLicense(TypeLicense.B);
        when(renterProfileService.getRenterProfileObj()).thenReturn(renter);
        when(offerRepository.findById(10L)).thenReturn(Optional.of(offer));

        VehicleReservationRequest tooMany = new VehicleReservationRequest(
                10L,
                LocalDateTime.of(2025, 10, 2, 10, 0),
                LocalDateTime.of(2025, 10, 2, 18, 0),
                5);

        assertThrows(IllegalArgumentException.class,
                () -> reservationService.createReservation(tooMany));
    }

    @Test
    void createReservation_whenNoSlotsAvailable() {
        renter.setTypeLicense(TypeLicense.B);
        when(renterProfileService.getRenterProfileObj()).thenReturn(renter);
        when(offerRepository.findById(10L)).thenReturn(Optional.of(offer));
        when(slotService.getSlotsWithinPeriod(anyLong(), any(), any())).thenReturn(Collections.emptyList());

        assertThrows(IllegalArgumentException.class,
                () -> reservationService.createReservation(request));
    }

    @Test
    void getMyReservation_success() {
        when(renterProfileService.getRenterProfileObj()).thenReturn(renter);
        VehicleReservation reservation = new VehicleReservation();
        reservation.setId(1L);
        when(reservationRepository.findByRenterId(1L)).thenReturn(List.of(reservation));
        VehicleReservationResponse mockResponse = mock(VehicleReservationResponse.class);
        when(reservationMapper.toResponse(reservation)).thenReturn(mockResponse);

        List<VehicleReservationResponse> result = reservationService.getMyReservation();

        assertEquals(1, result.size());
        verify(reservationMapper).toResponse(reservation);
    }
}