package com.more_than_code.go_con_coche.location.services;

import com.more_than_code.go_con_coche.global.EntityNotFoundException;
import com.more_than_code.go_con_coche.location.Location;
import com.more_than_code.go_con_coche.location.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl {
    private final LocationRepository locationRepository;

    public Location getLocationByIdObj(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Location", "id", String.valueOf(id)));
    }
}
