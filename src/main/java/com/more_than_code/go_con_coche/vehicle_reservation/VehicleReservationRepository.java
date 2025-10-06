package com.more_than_code.go_con_coche.vehicle_reservation;

import com.more_than_code.go_con_coche.vehicle_reservation.models.VehicleReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleReservationRepository extends JpaRepository<VehicleReservation, Long> {
    @Query("SELECT vr FROM VehicleReservation vr JOIN FETCH vr.rentalOffer WHERE vr.renter.id = :renterId")
    List<VehicleReservation> findByRenterId(@Param("renterId") Long renterId);
}