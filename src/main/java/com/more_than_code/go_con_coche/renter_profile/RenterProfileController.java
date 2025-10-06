package com.more_than_code.go_con_coche.renter_profile;

import com.more_than_code.go_con_coche.renter_profile.dtos.RenterProfileRequest;
import com.more_than_code.go_con_coche.renter_profile.dtos.RenterProfileUpdateRequest;
import com.more_than_code.go_con_coche.renter_profile.dtos.RenterProfileResponse;
import com.more_than_code.go_con_coche.renter_profile.services.RenterProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/renter-profiles")
@RequiredArgsConstructor
@Tag(name = "Renter Profiles", description = "Operations related to renters' profiles.")
public class RenterProfileController {

    private final RenterProfileService renterProfileService;

    @Operation(summary = "Create a new renter profile", description = "Creates a new renter profile with driver's license information. Allows uploading a profile image. Only accessible by users with RENTER role.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Renter profile created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RenterProfileResponse.class))), @ApiResponse(responseCode = "400", description = "Invalid input data - Check required fields", content = @Content), @ApiResponse(responseCode = "403", description = "Access denied - RENTER role required", content = @Content), @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token", content = @Content)})
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('RENTER')")
    public ResponseEntity<RenterProfileResponse> createRenterProfile(@Parameter(description = "Renter profile data including license type, number, expiration date and image", required = true) @Valid @ModelAttribute RenterProfileRequest renterProfileRequest) {
        RenterProfileResponse createdRenterProfile = renterProfileService.createRenterProfile(renterProfileRequest);
        return new ResponseEntity<>(createdRenterProfile, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all renter profiles", description = "Retrieves a list of all renter profiles registered in the system. Only accessible by administrators.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully retrieved list of profiles", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RenterProfileResponse.class))), @ApiResponse(responseCode = "403", description = "Access denied - ADMIN role required", content = @Content), @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token", content = @Content)})
    @GetMapping("")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<RenterProfileResponse>> getAllRenterProfiles() {
        List<RenterProfileResponse> renterProfileResponses = renterProfileService.getAllRenterProfiles();
        return new ResponseEntity<>(renterProfileResponses, HttpStatus.OK);
    }

    @Operation(summary = "Get own renter profile", description = "Retrieves the renter profile of the currently authenticated user. Only accessible by users with RENTER role.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully retrieved own profile", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RenterProfileResponse.class))), @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token", content = @Content), @ApiResponse(responseCode = "403", description = "Access denied - RENTER role required", content = @Content), @ApiResponse(responseCode = "404", description = "Renter profile not found for the authenticated user", content = @Content)})
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('RENTER')")
    public ResponseEntity<RenterProfileResponse> getMyRenterProfile() {
        RenterProfileResponse ownRenterProfile = renterProfileService.getMyRenterProfile();
        return new ResponseEntity<>(ownRenterProfile, HttpStatus.OK);
    }

    @Operation(summary = "Get renter profile by username", description = "Retrieves a renter profile by the associated user's username. Only accessible by administrators.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully retrieved renter profile", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RenterProfileResponse.class))), @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token", content = @Content), @ApiResponse(responseCode = "403", description = "Access denied - ADMIN role required", content = @Content), @ApiResponse(responseCode = "404", description = "Renter profile not found for the specified username", content = @Content)})
    @GetMapping("/{username}")
    @PreAuthorize("hasAnyRole('OWNER', 'RENTER')")
    public ResponseEntity<RenterProfileResponse> getRenterProfileByUsername(@Parameter(description = "Username of the registered user whose renter profile to retrieve", required = true, example = "renter1") @PathVariable String username) {
        RenterProfileResponse renterProfile = renterProfileService.getRenterProfileByUsername(username);
        return new ResponseEntity<>(renterProfile, HttpStatus.OK);
    }

    @Operation(summary = "Get renter profile by ID", description = "Retrieves a renter profile by its ID. Only accessible by administrators.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully retrieved renter profile", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RenterProfileResponse.class))), @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token", content = @Content), @ApiResponse(responseCode = "403", description = "Access denied - ADMIN role required", content = @Content), @ApiResponse(responseCode = "404", description = "Renter profile not found for the specified ID", content = @Content)})
    @GetMapping("/id/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<RenterProfileResponse> getRenterProfileById(@Parameter(description = "ID of the renter profile to retrieve", required = true, example = "1") @PathVariable Long id) {
        RenterProfileResponse renterProfile = renterProfileService.getRenterProfileById(id);
        return new ResponseEntity<>(renterProfile, HttpStatus.OK);
    }

    @Operation(summary = "Update own renter profile", description = "Update own renter profile with driver's license information. Allows uploading a profile image. Only accessible by users with RENTER role.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Renter profile updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RenterProfileResponse.class))), @ApiResponse(responseCode = "400", description = "Invalid input data - Check required fields", content = @Content), @ApiResponse(responseCode = "403", description = "Access denied - RENTER role required", content = @Content), @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token", content = @Content)})
    @PutMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('RENTER')")
    public ResponseEntity<RenterProfileResponse> updateRenterProfile(@Parameter(description = "Renter profile data including license type, number, expiration date and image") @Valid @ModelAttribute RenterProfileUpdateRequest renterProfileRequest) {
        RenterProfileResponse createdRenterProfile = renterProfileService.updateRenterProfile(renterProfileRequest);
        return new ResponseEntity<>(createdRenterProfile, HttpStatus.OK);
    }

    @Operation(summary = "Delete own renter profile", description = "Deletes the renter profile of the currently authenticated user. Only accessible by users with RENTER role.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Renter profile deleted successfully", content = @Content), @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token", content = @Content), @ApiResponse(responseCode = "403", description = "Access denied - RENTER role required", content = @Content), @ApiResponse(responseCode = "404", description = "Renter profile not found for the authenticated user", content = @Content)})
    @DeleteMapping("/me")
    @PreAuthorize("hasAnyRole('RENTER')")
    public ResponseEntity<Void> deleteMyRenterProfile() {
        renterProfileService.deleteMyRenterProfile();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Delete renter profile by ID", description = "Deletes a renter profile by its ID. Only accessible by administrators.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Renter profile deleted successfully", content = @Content), @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token", content = @Content), @ApiResponse(responseCode = "403", description = "Access denied - ADMIN role required", content = @Content), @ApiResponse(responseCode = "404", description = "Renter profile not found for the specified ID", content = @Content)})
    @DeleteMapping("/id/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Void> deleteRenterProfileById(@Parameter(description = "ID of the renter profile to delete", required = true, example = "1") @PathVariable Long id) {
        renterProfileService.deleteRenterProfileById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}