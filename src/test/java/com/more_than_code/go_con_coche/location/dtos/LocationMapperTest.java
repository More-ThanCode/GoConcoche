package com.more_than_code.go_con_coche.location.dtos;

import com.more_than_code.go_con_coche.location.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class LocationMapperTest {
    private LocationMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new LocationMapper();
    }

    @Test
    void toResponse_shouldMapAllFieldsCorrectly() {
        Location location = Location.builder()
                .id(1L)
                .city("Madrid")
                .district("Centro")
                .build();

        LocationResponse response = mapper.toResponse(location);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.city()).isEqualTo("Madrid");
        assertThat(response.district()).isEqualTo("Centro");
    }
}