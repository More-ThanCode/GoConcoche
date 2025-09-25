package com.more_than_code.go_con_coche.vehicle;

import com.more_than_code.go_con_coche.owner_profile.OwnerProfile;
import com.more_than_code.go_con_coche.vehicle.models.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    boolean existsByVin(String vin);
    boolean existsByPlateNumber(String plateNumber);
    boolean existsByInsuranceNumber(String insuranceNumber);
    List<Vehicle> findByOwner(OwnerProfile owner);
    boolean existsByVinAndIdIsNot(String vin, Long id);
    boolean existsByPlateNumberAndIdIsNot(String plateNumber, Long id);
    boolean existsByInsuranceNumberAndIdIsNot(String insuranceNumber, Long id);
}