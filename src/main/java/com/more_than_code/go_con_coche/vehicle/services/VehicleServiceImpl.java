package com.more_than_code.go_con_coche.vehicle.services;

import com.more_than_code.go_con_coche.cloudinary.CloudinaryService;
import com.more_than_code.go_con_coche.cloudinary.DefaultImageType;
import com.more_than_code.go_con_coche.cloudinary.UploadResult;
import com.more_than_code.go_con_coche.global.EntityAlreadyExistsException;
import com.more_than_code.go_con_coche.global.EntityNotFoundException;
import com.more_than_code.go_con_coche.global.UnauthorizedActionException;
import com.more_than_code.go_con_coche.owner_profile.OwnerProfile;
import com.more_than_code.go_con_coche.owner_profile.OwnerProfileRepository;
import com.more_than_code.go_con_coche.registered_user.RegisteredUser;
import com.more_than_code.go_con_coche.registered_user.services.UserAuthService;
import com.more_than_code.go_con_coche.vehicle.VehicleRepository;
import com.more_than_code.go_con_coche.vehicle.dtos.VehicleMapper;
import com.more_than_code.go_con_coche.vehicle.dtos.VehicleRequest;
import com.more_than_code.go_con_coche.vehicle.dtos.VehicleResponse;
import com.more_than_code.go_con_coche.vehicle.models.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService{

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;
    private final OwnerProfileRepository ownerProfileRepository;
    private final UserAuthService userAuthService;
    private final CloudinaryService cloudinaryService;

    @Override
    @Transactional
    public VehicleResponse createVehicle(VehicleRequest vehicleRequest) {
        RegisteredUser user = userAuthService.getAuthenticatedUser();
        if (vehicleRepository.existsByVin(vehicleRequest.vin())) {
            throw new EntityAlreadyExistsException("Vehicle", "VIN", vehicleRequest.vin());
        }
        if (vehicleRepository.existsByPlateNumber(vehicleRequest.plateNumber())) {
            throw new EntityAlreadyExistsException("Vehicle", "plate number", vehicleRequest.plateNumber());
        }
        if (vehicleRepository.existsByInsuranceNumber(vehicleRequest.insuranceNumber())) {
            throw new EntityAlreadyExistsException("Vehicle", "insurance number", vehicleRequest.insuranceNumber());
        }

        OwnerProfile owner = ownerProfileRepository.findByRegisteredUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("OwnerProfile", "id", user.getId().toString()));

        Vehicle vehicle = vehicleMapper.toEntity(vehicleRequest);
        vehicle.setOwner(owner);
        UploadResult uploadResult = cloudinaryService.resolveImage(vehicleRequest.image(), DefaultImageType.CAR);
        vehicle.setImageUrl(uploadResult.url());
        vehicle.setPublicImageId(uploadResult.publicId());

        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return vehicleMapper.toResponse(savedVehicle);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleResponse> getAllVehicles() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        return vehicles.stream()
                .map(vehicleMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleResponse> getVehicleByOwnerId(Long ownerId) {
        OwnerProfile owner = ownerProfileRepository.findById(ownerId)
                .orElseThrow(() -> new EntityNotFoundException("OwnerProfile", "id", ownerId.toString()));

        List<Vehicle> vehicles = vehicleRepository.findByOwner(owner);
        return vehicles.stream()
                .map(vehicleMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleResponse> getMyVehicles() {
        RegisteredUser user = userAuthService.getAuthenticatedUser();

        OwnerProfile owner = ownerProfileRepository.findByRegisteredUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("OwnerProfile", "user", user.getUsername()));

        List<Vehicle> vehicles = vehicleRepository.findByOwner(owner);

        return vehicles.stream()
                .map(vehicleMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public VehicleResponse updateVehicle(Long id, VehicleRequest vehicleRequest) {
        Vehicle existingVehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle", "id", id.toString()));

        RegisteredUser user = userAuthService.getAuthenticatedUser();

        if (!existingVehicle.getOwner().getRegisteredUser().getId().equals(user.getId())) {
            throw new UnauthorizedActionException("update", "Vehicle", id.toString());
        }

        if (vehicleRepository.existsByVinAndIdIsNot(vehicleRequest.vin(),id)) {
            throw new EntityAlreadyExistsException("Vehicle", "VIN", vehicleRequest.vin());
        }

        if (vehicleRepository.existsByPlateNumberAndIdIsNot(vehicleRequest.plateNumber(), id)) {
            throw new EntityAlreadyExistsException("Vehicle", "plate number", vehicleRequest.plateNumber());
        }

        if (vehicleRepository.existsByInsuranceNumberAndIdIsNot(vehicleRequest.insuranceNumber(), id)) {
            throw new EntityAlreadyExistsException("Vehicle", "insurance number", vehicleRequest.insuranceNumber());
        }

        vehicleMapper.updateFromDto(vehicleRequest, existingVehicle);

        if(vehicleRequest.image() != null && !vehicleRequest.image().isEmpty()) {

            if (existingVehicle.getPublicImageId() != null) {
                cloudinaryService.delete(existingVehicle.getPublicImageId());
            }

            UploadResult uploadResult = cloudinaryService.resolveImage(vehicleRequest.image(), DefaultImageType.CAR);
            existingVehicle.setImageUrl(uploadResult.url());
            existingVehicle.setPublicImageId(uploadResult.publicId());
        }

        Vehicle updatedVehicle = vehicleRepository.save(existingVehicle);
        return vehicleMapper.toResponse(updatedVehicle);
    }
}