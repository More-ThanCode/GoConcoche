package com.more_than_code.go_con_coche.vehicle_rental_offer.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.more_than_code.go_con_coche.vehicle_rental_offer.dtos.RentalOfferRequest;
import com.more_than_code.go_con_coche.vehicle_rental_offer.dtos.RentalOfferResponse;
import com.more_than_code.go_con_coche.vehicle_rental_offer.services.VehicleRentalOfferServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class VehicleRentalOfferControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VehicleRentalOfferServiceImpl rentalOfferService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "testuser", roles = {"OWNER"})
    void createRentalOffer_success() throws Exception {
        RentalOfferRequest request = new RentalOfferRequest(1L, 2L,
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), 50.0);

        RentalOfferResponse response = RentalOfferResponse.builder()
                .id(1L)
                .priceHour(20.0)
                .build();

        Mockito.when(rentalOfferService.createRenterOffer(any())).thenReturn(response);

        mockMvc.perform(post("/api/vehicle-rental-offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.priceHour").value(20.0));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"OWNER"})
    void getMyRentalOffers_success() throws Exception {
        RentalOfferResponse offer1 = RentalOfferResponse.builder().id(1L).priceHour(20.0).build();
        RentalOfferResponse offer2 = RentalOfferResponse.builder().id(2L).priceHour(30.0).build();

        Mockito.when(rentalOfferService.getMyRentalOffers()).thenReturn(List.of(offer1, offer2));

        mockMvc.perform(get("/api/vehicle-rental-offers/my-offers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].priceHour").value(30.0));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"OWNER"})
    void deleteRentalOffer_success() throws Exception {
        Long id = 100L;

        mockMvc.perform(delete("/api/vehicle-rental-offers/{id}", id))
                .andExpect(status().isNoContent());

        Mockito.verify(rentalOfferService).deleteRentalOffer(eq(id));
    }
}