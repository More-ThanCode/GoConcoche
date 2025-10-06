package com.more_than_code.go_con_coche.vehicle_rental_offer;

import com.more_than_code.go_con_coche.location.Location;
import com.more_than_code.go_con_coche.vehicle.models.Seater;
import com.more_than_code.go_con_coche.vehicle.models.Vehicle;
import com.more_than_code.go_con_coche.vehicle_rental_offer.dtos.search.SearchCriteria;
import com.more_than_code.go_con_coche.vehicle_rental_offer.models.VehicleRentalOffer;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDateTime;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class VehicleRentalOfferSpecificationTest {
    @Mock
    private Root<VehicleRentalOffer> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder  criteriaBuilder;

    @Mock
    private Join<VehicleRentalOffer, Vehicle> vehicleJoin;

    @Mock
    private Join<VehicleRentalOffer, Location> locationJoin;

    @Mock
    private Path<String> stringPath;

    @Mock
    private Path<Seater> seaterPath;

    @Mock
    private Path<LocalDateTime> dateTimePath;

    @BeforeEach
    void setup() {
        when(root.join(eq("vehicle"), any(JoinType.class))).thenReturn((Join) vehicleJoin);
        when(root.join(eq("location"), any(JoinType.class))).thenReturn((Join) locationJoin);
    }

    @Test
    void shouldAddIsAvailablePredicate_success() {
        SearchCriteria criteria = new SearchCriteria();
        Specification<VehicleRentalOffer> spec = VehicleRentalOfferSpecification.withCriteria(criteria);

        Predicate predicate = mock(Predicate.class);
        when(criteriaBuilder.isTrue(root.get("isAvailable"))).thenReturn(predicate);
        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

        spec.toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder).isTrue(root.get("isAvailable"));
        verify(criteriaBuilder).and(any(Predicate[].class));
    }

    @Test
    void shouldAddCityPredicate_success() {
        SearchCriteria criteria = new SearchCriteria();
        criteria.setCity("Kyiv");

        Predicate predicate = mock(Predicate.class);
        when(criteriaBuilder.isTrue(root.get("isAvailable"))).thenReturn(predicate);
        when(criteriaBuilder.lower(locationJoin.get("city"))).thenReturn(stringPath);
        when(criteriaBuilder.like(stringPath, "%kyiv%")).thenReturn(predicate);
        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

        VehicleRentalOfferSpecification.withCriteria(criteria)
                .toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder).like(stringPath, "%kyiv%");
    }

    @Test
    void shouldAddModelPredicate_success() {
        SearchCriteria criteria = new SearchCriteria();
        criteria.setModel("Tesla");

        Predicate predicate = mock(Predicate.class);
        when(criteriaBuilder.isTrue(root.get("isAvailable"))).thenReturn(predicate);
        when(criteriaBuilder.lower(vehicleJoin.get("model"))).thenReturn(stringPath);
        when(criteriaBuilder.like(stringPath, "%tesla%")).thenReturn(predicate);
        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

        VehicleRentalOfferSpecification.withCriteria(criteria)
                .toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder).like(stringPath, "%tesla%");
    }

    @Test
    void shouldAddSeatPredicate_success() {
        SearchCriteria criteria = new SearchCriteria();
        criteria.setSeats(4);

        Predicate predicate = mock(Predicate.class);
        when(criteriaBuilder.isTrue(root.get("isAvailable"))).thenReturn(predicate);
        when(vehicleJoin.get("seater")).thenReturn((Path) seaterPath);
        when(criteriaBuilder.equal(seaterPath, Seater.SEDAN)).thenReturn(predicate);
        when(criteriaBuilder.or(any(Predicate[].class))).thenReturn(predicate);
        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(predicate);

        VehicleRentalOfferSpecification.withCriteria(criteria)
                .toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder).or(any(Predicate[].class));
    }
}