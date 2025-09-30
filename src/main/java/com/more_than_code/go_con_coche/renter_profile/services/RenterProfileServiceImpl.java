package com.more_than_code.go_con_coche.renter_profile.services;

import com.more_than_code.go_con_coche.cloudinary.CloudinaryService;
import com.more_than_code.go_con_coche.cloudinary.DefaultImageType;
import com.more_than_code.go_con_coche.cloudinary.UploadResult;
import com.more_than_code.go_con_coche.global.EntityAlreadyExistsException;
import com.more_than_code.go_con_coche.registered_user.RegisteredUser;
import com.more_than_code.go_con_coche.registered_user.services.UserAuthService;
import com.more_than_code.go_con_coche.renter_profile.dtos.RenterProfileMapper;
import com.more_than_code.go_con_coche.renter_profile.RenterProfileRepository;
import com.more_than_code.go_con_coche.renter_profile.dtos.RenterProfileRequest;
import com.more_than_code.go_con_coche.renter_profile.dtos.RenterProfileResponse;

import com.more_than_code.go_con_coche.renter_profile.models.RenterProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RenterProfileServiceImpl implements RenterProfileService{
    private final RenterProfileRepository renterProfileRepository;
    private final UserAuthService userAuthService;
    private final RenterProfileMapper renterProfileMapper;
    private final CloudinaryService cloudinaryService;

    @Override
    @Transactional
    public RenterProfileResponse createRenterProfile(RenterProfileRequest renterProfileRequest) {

        RegisteredUser authenticatedUser = userAuthService.getAuthenticatedUser();

        if (renterProfileRepository.existsByRegisteredUser(authenticatedUser)){
            throw new EntityAlreadyExistsException(RenterProfile.class.getSimpleName(), "user", authenticatedUser.getUsername());
        }

        UploadResult uploadResult = cloudinaryService.resolveImage(renterProfileRequest.image(), DefaultImageType.PROFILE);

        RenterProfile renterProfile = RenterProfile.builder()
                .registeredUser(authenticatedUser)
                .typeLicense(renterProfileRequest.typeLicense())
                .licenseNumber(renterProfileRequest.licenseNumber())
                .expiredDate(renterProfileRequest.expiredDate())
                .imageURL(uploadResult.url())
                .publicImageId(uploadResult.publicId())
                .build();

        RenterProfile savedRenterProfile = renterProfileRepository.save(renterProfile);

        return renterProfileMapper.toResponse(savedRenterProfile);
    }
}
