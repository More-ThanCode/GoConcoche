package com.more_than_code.go_con_coche.renter_profile;

import com.more_than_code.go_con_coche.renter_profile.dtos.RenterProfileRequest;
import com.more_than_code.go_con_coche.renter_profile.dtos.RenterProfileResponse;
import com.more_than_code.go_con_coche.renter_profile.services.RenterProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/renter-profiles")
@RequiredArgsConstructor
public class RenterProfileController {

    private final RenterProfileService renterProfileService;

    @PostMapping("")
    public ResponseEntity<RenterProfileResponse> createRenterProfile(@Valid @ModelAttribute RenterProfileRequest renterProfileRequest) {
        RenterProfileResponse createdRenterProfile = renterProfileService.createRenterProfile(renterProfileRequest);
        return new ResponseEntity<>(createdRenterProfile, HttpStatus.CREATED);
    }
}
