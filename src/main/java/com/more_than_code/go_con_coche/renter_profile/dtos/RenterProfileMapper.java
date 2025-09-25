package com.more_than_code.go_con_coche.renter_profile.dtos;

import com.more_than_code.go_con_coche.renter_profile.models.RenterProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RenterProfileMapper {

    public RenterProfile toEntity(RenterProfileRequest request) {
        return RenterProfile.builder()
                .typeLicense(request.typeLicense())
                .licenseNumber(request.licenseNumber())
                .expiredDate(request.expiredDate())
                .build();
    }

    public RenterProfileResponse toResponse(RenterProfile renterProfile) {
        return RenterProfileResponse.builder()
                .id(renterProfile.getId())
                .registeredUser(renterProfile.getRegisteredUser().getId())
                .typeLicense(renterProfile.getTypeLicense().toString())
                .licenseNumber(renterProfile.getLicenseNumber())
                .expiredDate(renterProfile.getExpiredDate())
                .imageUrl(renterProfile.getImageURL())
                .build();
    }
}
