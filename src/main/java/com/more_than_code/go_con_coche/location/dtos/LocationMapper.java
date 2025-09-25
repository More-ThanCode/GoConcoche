package com.more_than_code.go_con_coche.location.dtos;

import com.more_than_code.go_con_coche.location.Location;
import org.springframework.stereotype.Component;

@Component
public class LocationMapper {
    public LocationResponse toResponse(Location location) {
        return new LocationResponse(location.getId(),
                location.getCity(),
                location.getDistrict());
    }

    public Location toEntity(LocationRequest request) {
        return Location.builder()
                .city(request.city())
                .district(request.district())
                .build();
    }
}
