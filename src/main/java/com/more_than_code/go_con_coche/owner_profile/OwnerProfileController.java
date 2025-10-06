package com.more_than_code.go_con_coche.owner_profile;

import com.more_than_code.go_con_coche.owner_profile.dtos.OwnerProfileRequest;
import com.more_than_code.go_con_coche.owner_profile.dtos.OwnerProfileResponse;
import com.more_than_code.go_con_coche.owner_profile.service.OwnerProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/owner-profiles")
@RequiredArgsConstructor
@Tag(name = "Owner Profiles", description = "Operations related to owners' profiles.")
public class OwnerProfileController {
    private final OwnerProfileService ownerProfileService;

    @Operation(summary = "Create a new owner profile", description = "Creates a profile for the authenticated owner")
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<OwnerProfileResponse> createOwnerProfile(
            @RequestPart("image") MultipartFile image) {
        OwnerProfileRequest request = new OwnerProfileRequest(image);
        OwnerProfileResponse createdOwnerProfile = ownerProfileService.createOwnerProfile(request);
        return new ResponseEntity<>(createdOwnerProfile, HttpStatus.CREATED);
    }

    @Operation(summary = "Get my owner profile", description = "Returns the profile of the authenticated owner")
    @GetMapping("/profile")
    public ResponseEntity<OwnerProfileResponse> getOwnerProfile() {
        OwnerProfileResponse ownerProfileResponse = ownerProfileService.getOwnerProfile();
        return ResponseEntity.ok(ownerProfileResponse);
    }

    @Operation(summary = "Get all owner profiles", description = "Returns all owner profiles (admin only)")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<OwnerProfileResponse>> getAllOwnerProfiles() {
        return ResponseEntity.ok(ownerProfileService.getAllOwnerProfiles());
    }

    @Operation(summary = "Get owner profile by ID", description = "Returns the owner profile for a given ID (admin only)")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<OwnerProfileResponse> getOwnerProfileById(@PathVariable Long id) {
        return ResponseEntity.ok(ownerProfileService.getOwnerProfileById(id));
    }

    @Operation(summary = "Update my owner profile", description = "Updates the profile of the authenticated owner")
    @PutMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<OwnerProfileResponse> updateMyOwnerProfile(
            @RequestPart(value = "image", required = false) MultipartFile image) {

        OwnerProfileRequest request = new OwnerProfileRequest(image);
        OwnerProfileResponse updatedProfile = ownerProfileService.updateMyOwnerProfile(request);

        return ResponseEntity.ok(updatedProfile);
    }

    @Operation(summary = "Delete my owner profile", description = "Deletes the profile of the authenticated owner")
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMyOwnerProfile() {
        ownerProfileService.deleteMyOwnerProfile();
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete owner profile by ID", description = "Deletes an owner profile by ID (admin only)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOwnerProfileById(@PathVariable Long id) {
        ownerProfileService.deleteOwnerProfileById(id);
        return ResponseEntity.noContent().build();
    }
}
