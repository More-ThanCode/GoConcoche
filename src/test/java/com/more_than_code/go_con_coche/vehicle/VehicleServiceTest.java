package com.more_than_code.go_con_coche.vehicle;

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
import com.more_than_code.go_con_coche.vehicle.dtos.VehicleMapper;
import com.more_than_code.go_con_coche.vehicle.dtos.VehicleRequest;
import com.more_than_code.go_con_coche.vehicle.dtos.VehicleResponse;
import com.more_than_code.go_con_coche.vehicle.models.FuelTypeCar;
import com.more_than_code.go_con_coche.vehicle.models.Seater;
import com.more_than_code.go_con_coche.vehicle.models.Vehicle;
import com.more_than_code.go_con_coche.vehicle.services.VehicleServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Vehicle Service Unit Tests")
public class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;
    @Mock
    private VehicleMapper vehicleMapper;
    @Mock
    private OwnerProfileRepository ownerProfileRepository;
    @Mock
    private UserAuthService userAuthService;
    @Mock
    private CloudinaryService cloudinaryService;

    @InjectMocks
    private VehicleServiceImpl vehicleService;

    private RegisteredUser user;
    private OwnerProfile ownerProfile;
    private Vehicle vehicle;
    private VehicleRequest vehicleRequest;
    private VehicleResponse vehicleResponse;
    private UploadResult uploadResult;

    @BeforeEach
    void setUp() {
        user = new RegisteredUser();
        user.setId(1L);
        user.setUsername("testUser");

        ownerProfile = new OwnerProfile();
        ownerProfile.setId(1L);
        ownerProfile.setRegisteredUser(user);

        MockMultipartFile mockFile = new MockMultipartFile(
                "image",
                "test-car.png",
                "image/jpg",
                "fake-car-image-content".getBytes()
        );

        uploadResult = new UploadResult("http://cloudinary.com/jeep.png", "vehicles/jeep123");

        vehicleRequest = VehicleRequest.builder()
                .vin("VIN123456789")
                .plateNumber("ABC123")
                .insuranceNumber("INS123")
                .model("Renegade")
                .brand("Jeep")
                .year(2023)
                .color("White")
                .seater(Seater.SUV)
                .childSeatsNumber(1)
                .fuelTypeCar(FuelTypeCar.DIESEL)
                .fuelConsumption("5.9L/100km")
                .image(mockFile)
                .build();

        vehicle = Vehicle.builder()
                .id(1L)
                .vin("VIN123456789")
                .plateNumber("ABC123")
                .insuranceNumber("INS123")
                .model("Renegade")
                .brand("Jeep")
                .year(2023)
                .color("White")
                .seater(Seater.SUV)
                .childSeatsNumber(1)
                .fuelTypeCar(FuelTypeCar.DIESEL)
                .fuelConsumption("5.9L/100km")
                .imageUrl("http://cloudinary.com/jeep.png")
                .publicImageId("vehicles/jeep123")
                .owner(ownerProfile)
                .build();

        vehicleResponse = VehicleResponse.builder()
                .id(1L)
                .vin("VIN123456789")
                .plateNumber("ABC123")
                .insuranceNumber("INS123")
                .model("Renegade")
                .brand("Jeep")
                .year(2023)
                .color("White")
                .seater(Seater.SUV)
                .childSeatsNumber(1)
                .fuelTypeCar(FuelTypeCar.DIESEL)
                .fuelConsumption("5.9L/100km")
                .imageUrl("http://cloudinary.com/jeep.png")
                .username("testUser")
                .build();
    }

    @Test
    @DisplayName("createVehicle - When valid request - Should return response")
    void createVehicle_WhenValidRequest_ShouldReturnResponse() {
        when(userAuthService.getAuthenticatedUser()).thenReturn(user);
        when(vehicleRepository.existsByVin(vehicleRequest.vin())).thenReturn(false);
        when(vehicleRepository.existsByPlateNumber(vehicleRequest.plateNumber())).thenReturn(false);
        when(vehicleRepository.existsByInsuranceNumber(vehicleRequest.insuranceNumber())).thenReturn(false);
        when(ownerProfileRepository.findByRegisteredUserId(user.getId())).thenReturn(Optional.of(ownerProfile));
        when(vehicleMapper.toEntity(vehicleRequest)).thenReturn(vehicle);
        when(cloudinaryService.resolveImage(vehicleRequest.image(), DefaultImageType.CAR)).thenReturn(uploadResult);
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);
        when(vehicleMapper.toResponse(vehicle)).thenReturn(vehicleResponse);

        VehicleResponse result = vehicleService.createVehicle(vehicleRequest);

        assertNotNull(result);
        assertEquals(vehicleResponse, result);
        verify(vehicleRepository).save(any(Vehicle.class));
        verify(cloudinaryService).resolveImage(vehicleRequest.image(), DefaultImageType.CAR);
    }

    @Test
    @DisplayName("create Vehicle - When VIN already exists - Should throw exception")
    void createVehicle_WhenVinAlreadyExists_ShouldThrowException() {
        when(userAuthService.getAuthenticatedUser()).thenReturn(user);
        when(vehicleRepository.existsByVin(vehicleRequest.vin())).thenReturn(true);

        assertThrows(EntityAlreadyExistsException.class,
                () -> vehicleService.createVehicle(vehicleRequest));

        verify(vehicleRepository, never()).save(any());
        verify(cloudinaryService, never()).resolveImage(any(), any());
    }

    @Test
    @DisplayName("createVehicle - When plate number already exists - Should throw exception")
    void createVehicle_WhenPlateNumberAlreadyExists_ShouldThrowException() {
        when(userAuthService.getAuthenticatedUser()).thenReturn(user);
        when(vehicleRepository.existsByVin(vehicleRequest.vin())).thenReturn(false);
        when(vehicleRepository.existsByPlateNumber(vehicleRequest.plateNumber())).thenReturn(true);

        assertThrows(EntityAlreadyExistsException.class,
                () -> vehicleService.createVehicle(vehicleRequest));

        verify(vehicleRepository, never()).save(any());
    }

    @Test
    @DisplayName("createVehicle - When insurance number already exists - Should throw exception")
    void createVehicle_WhenInsuranceNumberAlreadyExists_ShouldThrowException() {
        when(userAuthService.getAuthenticatedUser()).thenReturn(user);
        when(vehicleRepository.existsByVin(vehicleRequest.vin())).thenReturn(false);
        when(vehicleRepository.existsByPlateNumber(vehicleRequest.plateNumber())).thenReturn(false);
        when(vehicleRepository.existsByInsuranceNumber(vehicleRequest.insuranceNumber())).thenReturn(true);

        assertThrows(EntityAlreadyExistsException.class,
                () -> vehicleService.createVehicle(vehicleRequest));

        verify(vehicleRepository, never()).save(any());
    }

    @Test
    @DisplayName("createVehicle - When owner profile not found - Should throw exception")
    void createVehicle_WhenOwnerProfileNotFound_ShouldThrowException() {
        when(userAuthService.getAuthenticatedUser()).thenReturn(user);
        when(vehicleRepository.existsByVin(vehicleRequest.vin())).thenReturn(false);
        when(vehicleRepository.existsByPlateNumber(vehicleRequest.plateNumber())).thenReturn(false);
        when(vehicleRepository.existsByInsuranceNumber(vehicleRequest.insuranceNumber())).thenReturn(false);
        when(ownerProfileRepository.findByRegisteredUserId(user.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> vehicleService.createVehicle(vehicleRequest));

        verify(vehicleRepository, never()).save(any());
    }

    @Test
    @DisplayName("getAllVehicles - Should return all vehicles")
    void getAllVehicles_ShouldReturnAllVehicles() {
        List<Vehicle> vehicles = Arrays.asList(vehicle);
        when(vehicleRepository.findAll()).thenReturn(vehicles);
        when(vehicleMapper.toResponse(vehicle)).thenReturn(vehicleResponse);

        List<VehicleResponse> result = vehicleService.getAllVehicles();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(vehicleResponse, result.get(0));
    }

    @Test
    @DisplayName("getVehicleByOwnerId - When valid owner ID - Should return vehicles")
    void getVehiclesByOwnerId_WhenValidOwnerId_ShouldReturnVehicles() {
        Long ownerId = 1L;
        List<Vehicle> vehicles = Arrays.asList(vehicle);
        when(ownerProfileRepository.findById(ownerId)).thenReturn(Optional.of(ownerProfile));
        when(vehicleRepository.findByOwner(ownerProfile)).thenReturn(vehicles);
        when(vehicleMapper.toResponse(vehicle)).thenReturn(vehicleResponse);

        List<VehicleResponse> result = vehicleService.getVehicleByOwnerId(ownerId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(vehicleResponse, result.get(0));;
    }

    @Test
    @DisplayName("getVehicleByOwnerId - When owner not found - Should throw exception")
    void getVehicleByOwnerId_WhenOwnerNotFound_ShouldThrowException() {
        Long ownerId = 99L;
        when(ownerProfileRepository.findById(ownerId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> vehicleService.getVehicleByOwnerId(ownerId));
    }

    @Test
    @DisplayName("getMyVehicles - When valid user - Should return user's vehicles")
    void getMyVehicles_WhenValidUser_ShouldReturnUserVehicles() {
        List<Vehicle> vehicles = Arrays.asList(vehicle);
        when(userAuthService.getAuthenticatedUser()).thenReturn(user);
        when(ownerProfileRepository.findByRegisteredUserId(user.getId())).thenReturn(Optional.of(ownerProfile));
        when(vehicleRepository.findByOwner(ownerProfile)).thenReturn(vehicles);
        when(vehicleMapper.toResponse(vehicle)).thenReturn(vehicleResponse);

        List<VehicleResponse> result = vehicleService.getMyVehicles();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(vehicleResponse, result.get(0));
    }

    @Test
    @DisplayName("getMyVehicles - When user has no owner profile - Should throw exception")
    void getMyVehicles_WhenUserHasNoOwnerProfile_ShouldThrowException() {
        when(userAuthService.getAuthenticatedUser()).thenReturn(user);
        when(ownerProfileRepository.findByRegisteredUserId(user.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> vehicleService.getMyVehicles());
    }

    @Test
    @DisplayName("updateVehicle - When valid request - Should return updated response")
    void updateVehicle_WhenValidRequest_ShouldReturnUpdatedResponse() {
        Long vehicleId = 1L;
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle));
        when(userAuthService.getAuthenticatedUser()).thenReturn(user);
        when(vehicleRepository.existsByVinAndIdIsNot(vehicleRequest.vin(), vehicleId)).thenReturn(false);
        when(vehicleRepository.existsByPlateNumberAndIdIsNot(vehicleRequest.plateNumber(), vehicleId)).thenReturn(false);
        when(vehicleRepository.existsByInsuranceNumberAndIdIsNot(vehicleRequest.insuranceNumber(), vehicleId)).thenReturn(false);
        when(cloudinaryService.resolveImage(vehicleRequest.image(), DefaultImageType.CAR)).thenReturn(uploadResult);
        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);
        when(vehicleMapper.toResponse(vehicle)).thenReturn(vehicleResponse);

        VehicleResponse result = vehicleService.updateVehicle(vehicleId, vehicleRequest);

        assertNotNull(result);
        assertEquals(vehicleResponse, result);
        verify(vehicleMapper).updateFromDto(vehicleRequest, vehicle);
        verify(vehicleRepository).save(vehicle);
        verify(cloudinaryService).delete("vehicles/jeep123");
        verify(cloudinaryService).resolveImage(vehicleRequest.image(), DefaultImageType.CAR);
    }

    @Test
    @DisplayName("updateVehicle - When vehicle not found - Should throw exception")
    void updateVehicle_WhenVehicleNotFound_ShouldThrowException() {
        Long vehicleId = 99L;
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> vehicleService.updateVehicle(vehicleId, vehicleRequest));

        verify(vehicleRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateVehicle - When user is not owner - Should throw exception")
    void updateVehicle_WhenUserIsNotOwner_ShouldThrowException() {
        Long vehicleId = 1L;
        RegisteredUser differentUser = new RegisteredUser();
        differentUser.setId(2L);
        differentUser.setUsername("differentUser");

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle));
        when(userAuthService.getAuthenticatedUser()).thenReturn(differentUser);

        assertThrows(UnauthorizedActionException.class,
                () -> vehicleService.updateVehicle(vehicleId, vehicleRequest));

        verify(vehicleRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateVehicle - When VIN already exists for different vehicle - Should throw exception")
    void updateVehicle_WhenVinAlreadyExistsForDifferentVehicle_ShouldThrowException() {
        Long vehicleId = 1L;
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle));
        when(userAuthService.getAuthenticatedUser()).thenReturn(user);
        when(vehicleRepository.existsByVinAndIdIsNot(vehicleRequest.vin(), vehicleId)).thenReturn(true);

        assertThrows(EntityAlreadyExistsException.class,
                () -> vehicleService.updateVehicle(vehicleId, vehicleRequest));

        verify(vehicleRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateVehicle - When plate number already exists for different vehicle - Should throw exception")
    void updateVehicle_WhenPlateNumberAlreadyExistsForDifferentVehicle_ShouldThrowException() {
        Long vehicleId = 1L;
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle));
        when(userAuthService.getAuthenticatedUser()).thenReturn(user);
        when(vehicleRepository.existsByVinAndIdIsNot(vehicleRequest.vin(), vehicleId)).thenReturn(false);
        when(vehicleRepository.existsByPlateNumberAndIdIsNot(vehicleRequest.plateNumber(), vehicleId)).thenReturn(true);

        assertThrows(EntityAlreadyExistsException.class,
                () -> vehicleService.updateVehicle(vehicleId, vehicleRequest));

        verify(vehicleRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateVehicle - When no image provided - Should not update image")
    void updateVehicle_WhenNoImageProvided_ShouldNotUpdateImage() {
        Long vehicleId = 1L;
        VehicleRequest requestWithoutImage = VehicleRequest.builder()
                .vin(vehicleRequest.vin())
                .plateNumber(vehicleRequest.plateNumber())
                .insuranceNumber(vehicleRequest.insuranceNumber())
                .model(vehicleRequest.model())
                .brand(vehicleRequest.brand())
                .year(vehicleRequest.year())
                .color(vehicleRequest.color())
                .seater(vehicleRequest.seater())
                .childSeatsNumber(vehicleRequest.childSeatsNumber())
                .fuelTypeCar(vehicleRequest.fuelTypeCar())
                .fuelConsumption(vehicleRequest.fuelConsumption())
                .image(null)
                .build();

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle));
        when(userAuthService.getAuthenticatedUser()).thenReturn(user);
        when(vehicleRepository.existsByVinAndIdIsNot(vehicleRequest.vin(), vehicleId)).thenReturn(false);
        when(vehicleRepository.existsByPlateNumberAndIdIsNot(vehicleRequest.plateNumber(), vehicleId)).thenReturn(false);
        when(vehicleRepository.existsByInsuranceNumberAndIdIsNot(vehicleRequest.insuranceNumber(), vehicleId)).thenReturn(false);
        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);
        when(vehicleMapper.toResponse(vehicle)).thenReturn(vehicleResponse);

        VehicleResponse result = vehicleService.updateVehicle(vehicleId, requestWithoutImage);

        assertNotNull(result);
        verify(cloudinaryService, never()).delete(anyString());
        verify(cloudinaryService, never()).resolveImage(any(), any());
    }

    @Test
    @DisplayName("deleteVehicle - When valid request - Should delete vehicle")
    void deleteVehicle_WhenValidRequest_ShouldDeleteVehicle() {
        Long vehicleId = 1L;
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle));
        when(userAuthService.getAuthenticatedUser()).thenReturn(user);
        when(cloudinaryService.delete("vehicles/jeep123")).thenReturn(true);

        vehicleService.deleteVehicle(vehicleId);

        verify(vehicleRepository).delete(vehicle);
        verify(cloudinaryService).delete("vehicles/jeep123");
    }

    @Test
    @DisplayName("deleteVehicle - When vehicle not found - Should throw exception")
    void deleteVehicle_WhenVehicleNotFound_ShouldThrowException() {
        Long vehicleId = 99L;
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> vehicleService.deleteVehicle(vehicleId));

        verify(vehicleRepository, never()).delete(any());
    }

    @Test
    @DisplayName("deleteVehicle - When user is not owner - Should throw exception")
    void deleteVehicle_WhenUserIsNotOwner_ShouldThrowException() {
        Long vehicleId = 1L;
        RegisteredUser differentUser = new RegisteredUser();
        differentUser.setId(2L);
        differentUser.setUsername("differentUser");

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle));
        when(userAuthService.getAuthenticatedUser()).thenReturn(differentUser);

        assertThrows(UnauthorizedActionException.class,
                () -> vehicleService.deleteVehicle(vehicleId));

        verify(vehicleRepository, never()). delete(any());
    }

    @Test
    @DisplayName("deleteVehicle - When vehicle has no public image ID - Should not delete from cloudinary")
    void deleteVehicle_WhenVehicleHasNoPublicImageId_ShouldNotDeleteFromCloudinary() {
        Long vehicleId = 1L;
        vehicle.setPublicImageId(null);
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle));
        when(userAuthService.getAuthenticatedUser()).thenReturn(user);

        vehicleService.deleteVehicle(vehicleId);

        verify(vehicleRepository).delete(vehicle);
        verify(cloudinaryService, never()).delete(anyString());
    }

    @Test
    @DisplayName("getVehicleByIdObj - When vehicle exists - Should return vehicle")
    void getVehicleByIdObj_WhenVehicleExists_ShouldReturnVehicle() {
        Long vehicleId = 1L;
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle));

        Vehicle result = vehicleService.getVehicleByIdObj(vehicleId);

        assertNotNull(result);
        assertEquals(vehicleId, result.getId());
        verify(vehicleRepository, times(1)).findById(vehicleId);
    }

    @Test
    @DisplayName("getVehicleByIdObj - When vehicle does not exist - Should throw exception")
    void getVehicleByIdObj_WhenVehicleDoesNotExist_ShouldThrowException() {
        Long vehicleId = 99L;
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> vehicleService.getVehicleByIdObj(vehicleId));
        verify(vehicleRepository, times(1)).findById(vehicleId);
    }

}