package com.more_than_code.go_con_coche.renter_profile.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.more_than_code.go_con_coche.renter_profile.models.TypeLicense;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public record RenterProfileRequest(
        @NotNull(message = "License type is mandatory")
        TypeLicense typeLicense,

        @NotBlank(message = "License number is mandatory")
        String licenseNumber,

        @Future(message = "The license can't be expired")
        @JsonFormat(pattern = "dd-MM-yyyy")
        LocalDate expiredDate,

        MultipartFile image
) {
}
