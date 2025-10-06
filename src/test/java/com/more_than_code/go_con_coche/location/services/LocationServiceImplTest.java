package com.more_than_code.go_con_coche.location.services;
import com.more_than_code.go_con_coche.location.Location;
import com.more_than_code.go_con_coche.location.LocationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class LocationServiceImplTest {
    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private LocationServiceImpl locationService;

    @Test
    void getLocationByIdObj_success() {
        Long id = 1L;
        Location location = Location.builder()
                .id(id)
                .city("Kyiv")
                .district("Shevchenkivskyi")
                .address("Khreshchatyk 1")
                .build();

        when(locationRepository.findById(id)).thenReturn(Optional.of(location));

        Location result = locationService.getLocationByIdObj(id);

        assertNotNull(result);
        assertEquals("Kyiv", result.getCity());
        assertEquals("Shevchenkivskyi", result.getDistrict());
        assertEquals("Khreshchatyk 1", result.getAddress());
        verify(locationRepository, times(1)).findById(id);
    }
}