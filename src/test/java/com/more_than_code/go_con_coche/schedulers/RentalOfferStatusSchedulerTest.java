package com.more_than_code.go_con_coche.schedulers;

import com.more_than_code.go_con_coche.vehicle_rental_offer.models.VehicleRentalOffer;
import com.more_than_code.go_con_coche.vehicle_rental_offer.repositories.VehicleRentalOfferRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.List;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentalOfferStatusSchedulerTest {
    @Mock
    private VehicleRentalOfferRepository offerRepository;

    @InjectMocks
    private RentalOfferStatusScheduler scheduler;

    @Test
    void updateOfferStatus_success() {
        VehicleRentalOffer offer1 = mock(VehicleRentalOffer.class);
        VehicleRentalOffer offer2 = mock(VehicleRentalOffer.class);

        when(offerRepository.findByEndDateTimeBeforeAndIsAvailableTrue(any(LocalDateTime.class)))
                .thenReturn(List.of(offer1, offer2));

        scheduler.updateFlightStatuses();

        verify(offer1, times(1)).updateStatusIfNeeded();
        verify(offer2, times(1)).updateStatusIfNeeded();
        verify(offerRepository, times(1)).saveAll(List.of(offer1, offer2));
    }

    @Test
    void updateOfferStatus_WhenNoOffers() {
        when(offerRepository.findByEndDateTimeBeforeAndIsAvailableTrue(any(LocalDateTime.class)))
                .thenReturn(List.of());

        scheduler.updateFlightStatuses();

        verify(offerRepository, times(1)).findByEndDateTimeBeforeAndIsAvailableTrue(any(LocalDateTime.class));
        verify(offerRepository, times(1)).saveAll(List.of());
    }
}
