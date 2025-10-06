package com.more_than_code.go_con_coche.vehicle_reservation.services;

import com.more_than_code.go_con_coche.vehicle_reservation.dtos.VehicleReservationRequest;
import com.more_than_code.go_con_coche.vehicle_reservation.dtos.VehicleReservationResponse;

import java.util.List;

public interface VehicleReservationService {

    VehicleReservationResponse createReservation(VehicleReservationRequest request);
    List<VehicleReservationResponse> getMyReservation();
}