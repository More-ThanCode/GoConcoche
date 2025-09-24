package com.more_than_code.go_con_coche.vehicle.services;

import com.more_than_code.go_con_coche.vehicle.dtos.VehicleRequest;
import com.more_than_code.go_con_coche.vehicle.dtos.VehicleResponse;

import java.util.List;

public interface VehicleService {

    VehicleResponse createVehicle(VehicleRequest vehicleRequest);
    List<VehicleResponse> getAllVehicles();
    List<VehicleResponse> getVehicleByOwnerId(Long ownerId);
    List<VehicleResponse> getMyVehicles();
    VehicleResponse updateVehicle(Long id, VehicleRequest vehicleRequest);
    void deleteVehicle(Long id);
}