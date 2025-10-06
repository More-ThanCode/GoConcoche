package com.more_than_code.go_con_coche.vehicle;

import com.more_than_code.go_con_coche.vehicle.dtos.VehicleRequest;
import com.more_than_code.go_con_coche.vehicle.dtos.VehicleResponse;
import com.more_than_code.go_con_coche.vehicle.models.FuelTypeCar;
import com.more_than_code.go_con_coche.vehicle.models.Seater;
import com.more_than_code.go_con_coche.vehicle.services.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class VehicleControllerTest {

    private MockMvc mockMvc;

    @Mock
    private VehicleService vehicleService;

    @InjectMocks
    private VehicleController vehicleController;

    private VehicleResponse vehicleResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(vehicleController).build();

        vehicleResponse = new VehicleResponse(1L, "1HGCM82633A004352", "1234ABC", "INS-987654333", "Civic", "Honda", 2022, "red", Seater.SEDAN, 2, FuelTypeCar.DIESEL, "6.5 L/100km", "www.image.com", "owner");
    }

    @Test
    void createVehicle_ShouldReturnCreatedVehicle() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image", "car.jpg", "image/jpeg", "fake-image".getBytes());

        doReturn(vehicleResponse).when(vehicleService).createVehicle(any(VehicleRequest.class));

        mockMvc.perform(multipart("/api/vehicles")
                        .file(image)
                        .param("brand", "Honda")
                        .param("model", "Civic")
                        .param("year", "2022")
                        .param("color", "red")
                        .param("seats", "SEDAN")
                        .param("childSeatsNumber", "2")
                        .param("fuelTypeCar", "DIESEL")
                        .param("fuelConsumption", "6.5 L/100km")
                        .param("plateNumber", "1234ABC")
                        .param("vin", "1HGCM82633A004352")
                        .param("insuranceNumber", "INS-987654333")
                        .param("seater", "SEDAN")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.brand").value("Honda"))
                .andExpect(jsonPath("$.model").value("Civic"));


        verify(vehicleService, times(1)).createVehicle(any(VehicleRequest.class));
    }

    @Test
    void getAllVehicles_ShouldReturnListOfVehicles() throws Exception {
        when(vehicleService.getAllVehicles()).thenReturn(List.of(vehicleResponse));

        mockMvc.perform(get("/api/vehicles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].brand").value("Honda"))
                .andExpect(jsonPath("$[0].fuelTypeCar").value("DIESEL"));

        verify(vehicleService, times(1)).getAllVehicles();
    }

    @Test
    void getVehiclesByOwnerId_ShouldReturnVehiclesOfOwner() throws Exception {
        when(vehicleService.getVehicleByOwnerId(1L)).thenReturn(List.of(vehicleResponse));

        mockMvc.perform(get("/api/vehicles/owner/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].model").value("Civic"))
                .andExpect(jsonPath("$[0].brand").value("Honda"));

        verify(vehicleService, times(1)).getVehicleByOwnerId(1L);
    }

    @Test
    void getMyVehicles_ShouldReturnMyVehicles() throws Exception {
        when(vehicleService.getMyVehicles()).thenReturn(List.of(vehicleResponse));

        mockMvc.perform(get("/api/vehicles/my"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].brand").value("Honda"))
                .andExpect(jsonPath("$[0].fuelTypeCar").value("DIESEL"));

        verify(vehicleService, times(1)).getMyVehicles();
    }

    @Test
    void updateVehicle_ShouldReturnUpdatedVehicle() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image", "car.jpg", "image/jpeg", "fake-image".getBytes());

        doReturn(vehicleResponse).when(vehicleService).updateVehicle(eq(1L), any(VehicleRequest.class));

        mockMvc.perform(multipart("/api/vehicles/{id}", 1L)
                        .file(image)
                        .param("brand", "Honda")
                        .param("model", "Civic")
                        .param("year", "2022")
                        .param("color", "red")
                        .param("seats", "SEDAN")
                        .param("childSeatsNumber", "2")
                        .param("fuelTypeCar", "DIESEL")
                        .param("fuelConsumption", "6.5 L/100km")
                        .param("plateNumber", "1234ABC")
                        .param("vin", "1HGCM82633A004352")
                        .param("insuranceNumber", "INS-987654333")
                        .param("seater", "SEDAN")
                        .with(req -> { req.setMethod("PUT"); return req; })
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model").value("Civic"))
                .andExpect(jsonPath("$.color").value("red"))
                .andExpect(jsonPath("$.fuelTypeCar").value("DIESEL"));


        verify(vehicleService, times(1)).updateVehicle(eq(1L), any(VehicleRequest.class));
    }

    @Test
    void deleteVehicle_ShouldReturnNoContent() throws Exception {
        doNothing().when(vehicleService).deleteVehicle(1L);

        mockMvc.perform(delete("/api/vehicles/1"))
                .andExpect(status().isNoContent());

        verify(vehicleService, times(1)).deleteVehicle(1L);
    }

}
