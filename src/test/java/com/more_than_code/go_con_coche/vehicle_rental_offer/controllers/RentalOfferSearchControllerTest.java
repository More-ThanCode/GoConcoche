package com.more_than_code.go_con_coche.vehicle_rental_offer.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.more_than_code.go_con_coche.vehicle_rental_offer.dtos.search.SearchCriteria;
import com.more_than_code.go_con_coche.vehicle_rental_offer.dtos.search.SearchOfferResponse;
import com.more_than_code.go_con_coche.vehicle_rental_offer.services.RentalOfferSearchService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RentalOfferSearchControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RentalOfferSearchService searchService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void searchOffersByCriteria_allParams_success() throws Exception {
        SearchOfferResponse offer1 = SearchOfferResponse.builder()
                .offerId(1L)
                .vehicleModel("Model 3")
                .vehicleBrand("Tesla")
                .city("Kyiv")
                .startDateTime(LocalDateTime.of(2025, 10, 1, 10, 0))
                .endDateTime(LocalDateTime.of(2025, 10, 2, 10, 0))
                .priceHour(25.0)
                .build();

        SearchOfferResponse offer2 = SearchOfferResponse.builder()
                .offerId(2L)
                .vehicleModel("BMW X5")
                .vehicleBrand("BMW")
                .city("Kyiv")
                .startDateTime(LocalDateTime.of(2025, 10, 1, 10, 0))
                .endDateTime(LocalDateTime.of(2025, 10, 2, 10, 0))
                .priceHour(30.0)
                .build();

        Mockito.when(searchService.searchWithCriteria(any(SearchCriteria.class)))
                .thenReturn(List.of(offer1, offer2));

        mockMvc.perform(get("/api/vehicle-rental-offers/search/by-criteria")
                        .param("city", "Kyiv")
                        .param("model", "Tesla")
                        .param("startDateTime", "2025-10-01T10:00:00")
                        .param("endDateTime", "2025-10-02T10:00:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].offerId").value(1))
                .andExpect(jsonPath("$[1].vehicleBrand").value("BMW"))
                .andExpect(jsonPath("$[1].priceHour").value(30.0));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void searchOffersByCriteria_noParams() throws Exception {
        SearchOfferResponse offer = SearchOfferResponse.builder()
                .offerId(1L)
                .vehicleModel("Toyota Corolla")
                .vehicleBrand("Toyota")
                .city("Lviv")
                .startDateTime(LocalDateTime.of(2025, 10, 1, 10, 0))
                .endDateTime(LocalDateTime.of(2025, 10, 2, 10, 0))
                .priceHour(20.0)
                .build();

        Mockito.when(searchService.searchWithCriteria(any(SearchCriteria.class)))
                .thenReturn(List.of(offer));

        mockMvc.perform(get("/api/vehicle-rental-offers/search/by-criteria"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].city").value("Lviv"))
                .andExpect(jsonPath("$[0].vehicleBrand").value("Toyota"));
    }
}