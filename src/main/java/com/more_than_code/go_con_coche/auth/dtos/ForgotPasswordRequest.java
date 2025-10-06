package com.more_than_code.go_con_coche.auth.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request to initiate password reset")
public record ForgotPasswordRequest(
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Email should be valid")
        @Schema(description = "Your registered email", example = "user@example.com")
        String email
) {}
