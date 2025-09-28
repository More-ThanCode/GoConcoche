package com.more_than_code.go_con_coche.owner_profile.service;

import com.more_than_code.go_con_coche.global.EntityAlreadyExistsException;
import com.more_than_code.go_con_coche.owner_profile.OwnerProfile;
import com.more_than_code.go_con_coche.owner_profile.OwnerProfileRepository;
import com.more_than_code.go_con_coche.owner_profile.dtos.OwnerProfileMapper;
import com.more_than_code.go_con_coche.owner_profile.dtos.OwnerProfileRequest;
import com.more_than_code.go_con_coche.owner_profile.dtos.OwnerProfileResponse;
import com.more_than_code.go_con_coche.registered_user.RegisteredUser;
import com.more_than_code.go_con_coche.registered_user.services.UserAuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OwnerProfileServiceImpl implements OwnerProfileService{
    private final OwnerProfileRepository ownerProfileRepository;
    private final UserAuthService userAuthService;
    private final OwnerProfileMapper ownerProfileMapper;

    @Override
    @Transactional
    public OwnerProfileResponse createOwnerProfile (OwnerProfileRequest ownerProfileRequest){
        OwnerProfile ownerProfile = ownerProfileMapper.toEntity(ownerProfileRequest);
        RegisteredUser user = userAuthService.getAuthenticatedUser();
        ownerProfile.setRegisteredUser(user);

        if(ownerProfileRepository.findByRegisteredUserId(user)){
            throw  new EntityAlreadyExistsException(OwnerProfile.class.getSimpleName(), "user", user.getUsername());
        }

        OwnerProfile savedOwnerProfile = ownerProfileRepository.save(ownerProfile);
        return ownerProfileMapper.toResponse(savedOwnerProfile);
    }
}
