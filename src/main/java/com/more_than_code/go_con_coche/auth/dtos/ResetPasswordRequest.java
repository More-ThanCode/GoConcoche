package com.more_than_code.go_con_coche.auth.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Request to reset password with token")
public record ResetPasswordRequest(
        @NotBlank(message = "Token cannot be blank")
        @Schema(description = "Password reset token from email")
        String token,

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 8, message = "Password should be at least 8 characters long")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{8,}$",
                message = "Password must include uppercase, lowercase, number and special character")
        @Schema(description = "New password", example = "NewPassword123!")
        String newPassword
) {}
