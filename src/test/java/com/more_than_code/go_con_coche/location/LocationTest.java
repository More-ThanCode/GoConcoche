package com.more_than_code.go_con_coche.location;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class LocationTest {
    @Test
    void testBuilderAndGetters() {
        Location location = Location.builder()
                .id(1L)
                .city("Madrid")
                .district("Centro")
                .build();

        assertThat(location.getId()).isEqualTo(1L);
        assertThat(location.getCity()).isEqualTo("Madrid");
        assertThat(location.getDistrict()).isEqualTo("Centro");
    }

    @Test
    void testSettersAndEquals() {
        Location loc1 = new Location();
        loc1.setId(1L);
        loc1.setCity("Barcelona");
        loc1.setDistrict("Gracia");

        Location loc2 = new Location(1L, "Barcelona", "Gracia");

        assertThat(loc1).isEqualTo(loc2);
        assertThat(loc1.hashCode()).isEqualTo(loc2.hashCode());
    }

    @Test
    void testToString() {
        Location location = Location.builder()
                .id(5L)
                .city("Valencia")
                .district("Ruzafa")
                .build();

        String result = location.toString();

        assertThat(result).contains("Valencia");
        assertThat(result).contains("Ruzafa");
    }
}