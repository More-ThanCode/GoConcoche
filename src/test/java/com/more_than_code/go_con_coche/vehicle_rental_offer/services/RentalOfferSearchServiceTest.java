package com.more_than_code.go_con_coche.vehicle_rental_offer.services;

import com.more_than_code.go_con_coche.location.Location;
import com.more_than_code.go_con_coche.vehicle.models.Vehicle;
import com.more_than_code.go_con_coche.vehicle_rental_offer.VehicleRentalOfferSpecification;
import com.more_than_code.go_con_coche.vehicle_rental_offer.dtos.search.SearchCriteria;
import com.more_than_code.go_con_coche.vehicle_rental_offer.dtos.search.SearchOfferResponse;
import com.more_than_code.go_con_coche.vehicle_rental_offer.models.RentalOfferSlot;
import com.more_than_code.go_con_coche.vehicle_rental_offer.models.VehicleRentalOffer;
import com.more_than_code.go_con_coche.vehicle_rental_offer.repositories.VehicleRentalOfferRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.*;

import static com.more_than_code.go_con_coche.vehicle.models.Seater.SEDAN;
import static com.more_than_code.go_con_coche.vehicle.models.Seater.SUV;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RentalOfferSearchService Unit Tests")
public class RentalOfferSearchServiceTest {

    @Mock
    private VehicleRentalOfferRepository offerRepository;
    @Mock
    private RentalOfferSlotService slotService;
    @Mock
    private Specification<VehicleRentalOffer> mockSpecification;

    @InjectMocks
    private RentalOfferSearchService searchService;

    private SearchCriteria criteria;
    private VehicleRentalOffer offer1, offer2;
    private final LocalDateTime FROM = LocalDateTime.of(2025, 12, 1, 10, 0);
    private final LocalDateTime TO = LocalDateTime.of(2025, 12, 1, 12, 0);

    @BeforeEach
    void setUp() {
        Vehicle vehicle1 = Vehicle.builder().id(100L).model("Model X").brand("Brand Y").seater(SEDAN).build();
        Location location = Location.builder().id(200L).city("Valencia").build();

        offer1 = VehicleRentalOffer.builder()
                .id(1L).vehicle(vehicle1).location(location).priceHour(10.0).build();

        Vehicle vehicle2 = Vehicle.builder().id(101L).model("Model Y").brand("Brand Z").seater(SUV).build();
        offer2 = VehicleRentalOffer.builder()
                .id(2L).vehicle(vehicle1).location(location).priceHour(20.0).build();

        criteria = SearchCriteria.builder().startDateTime(FROM).endDateTime(TO).city("Valencia").build();
    }

    @Test
    @DisplayName("searchWithCriteria - When all criteria are present - Should call Specification and filter by slots")
    void searchWithCriteria_WhenAllCriteriaPresent_ShouldUseSpecificationAndFilterBySlots() {
        try (MockedStatic<VehicleRentalOfferSpecification> mockedSpec = mockStatic(VehicleRentalOfferSpecification.class)) {
            mockedSpec.when(() -> VehicleRentalOfferSpecification.withCriteria(criteria))
                    .thenReturn(mockSpecification);
            when(offerRepository.findAll(mockSpecification)).thenReturn(List.of(offer1, offer2));

            RentalOfferSlot availableSlot = RentalOfferSlot.builder().isAvailable(true).build();
            RentalOfferSlot unavailableSlot = RentalOfferSlot.builder().isAvailable(false).build();

            when(slotService.getSlotsWithinPeriod(offer1.getId(), FROM, TO))
                    .thenReturn(List.of(availableSlot));
            when(slotService.getSlotsWithinPeriod(offer2.getId(), FROM, TO))
                    .thenReturn(List.of(unavailableSlot));

            List <SearchOfferResponse> result = searchService.searchWithCriteria(criteria);

            assertNotNull(result);
            assertEquals(1, result.size(), "Only offer 1 should be returned due to slot filtering.");
            assertEquals(offer1.getId(), result.get(0).offerId());

            mockedSpec.verify(() -> VehicleRentalOfferSpecification.withCriteria(criteria));
            verify(offerRepository).findAll(mockSpecification);
            verify(slotService, times(2)).getSlotsWithinPeriod(anyLong(), eq(FROM), eq(TO));
        }
    }

    @Test
    @DisplayName("searchWithCriteria - When offer is filtered out by slot avialability - Shoud not be included in result")
    void searchWithCriteria_WhenOfferLacksAvailableSlots_ShouldBeFilteredOut() {

        try (MockedStatic<VehicleRentalOfferSpecification> mockedSpec = mockStatic(VehicleRentalOfferSpecification.class)) {
            mockedSpec.when(() -> VehicleRentalOfferSpecification.withCriteria(criteria))
                    .thenReturn(mockSpecification);
            when(offerRepository.findAll(mockSpecification)).thenReturn(List.of(offer1, offer2));
            when(slotService.getSlotsWithinPeriod(offer1.getId(), FROM, TO))
                    .thenReturn(Collections.emptyList());

            RentalOfferSlot unavailableSlot = RentalOfferSlot.builder().isAvailable(false).build();
            when(slotService.getSlotsWithinPeriod(offer2.getId(), FROM, TO))
                    .thenReturn(List.of(unavailableSlot));

            List<SearchOfferResponse> result = searchService.searchWithCriteria(criteria);

            assertNotNull(result);
            assertTrue(result.isEmpty(), "All offers should be filtered out because they lack available slots in the requested period.");
        }
    }
}