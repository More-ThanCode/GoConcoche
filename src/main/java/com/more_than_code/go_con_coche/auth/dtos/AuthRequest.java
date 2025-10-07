package com.more_than_code.go_con_coche.auth.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request to create a user login")
public record AuthRequest(
        @NotBlank(message = "Username cannot be blank")
        @Size(max = 30, message = "Username cannot be longer than 30 characters!")
        @Schema(description = "Your username", example = "niaofnarnia")
        String username,

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 8, message = "Password should be at least 8 characters long!")
        @Schema(description = "Your password", example = "Password123!")
        String password) {
}
