package com.more_than_code.go_con_coche.auth.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

@Schema(description = "Request to create a user registration")
public record RegisterRequest(
        @NotBlank
        @Size(min = 2, max = 50)
        @Pattern(regexp = "^[A-Za-z'-]+$", message = "First name must contain only Latin letters, hyphen or apostrophe")
        @Schema(description = "Your first name", example = "Nia")
        String firstName,

        @NotBlank
        @Size(min = 2, max = 50)
        @Pattern(regexp = "^[A-Za-z'-]+$", message = "Second name must contain only Latin letters, hyphen or apostrophe")
        @Schema(description = "Your last name", example = "Espinal")
        String lastName,

        @NotNull
        @Past(message = "Date of birth must be in the past")
        @Schema(description = "Your date of birth (format: yyyy-MM-dd)", example = "1994-12-31")
        LocalDate dateOfBirth,

        @NotNull
        @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must be valid and contain 10–15 digits (optionally starting with +)")
        @Schema(description = "Your unique phone number(optionally starting with +)", example = "+34655818589")
        String phoneNumber,

        @NotBlank(message = "Username cannot be blank") @Size(max = 30, message = "Username cannot be longer than 30 characters!")
        @Schema(description = "Unique username", example = "niaofnarnia")
        String username,

        @NotBlank(message = "Email cannot be blank") @Email(message = "Email should be valid!")
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Email format is invalid")
        @Schema(description = "Unique email", example = "niaofnarnia@gmail.com")
        String email,

        @NotBlank(message = "Password cannot be blank") @Size(min = 8, message = "Password should be at least 8 characters long!")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{8,}$", message = "Password must be at least 8 characters and include uppercase, lowercase, number and special character")
        @Schema(description = "Your Password must be at least 8 characters and include uppercase, lowercase, number and special character", example = "Password123!")
        String password,

        @NotEmpty(message = "Roles cannot be empty")
        @Schema(description = "Add your roles", example = "[2,3]", type = "array", implementation = Long.class)
        Set<Long> roleIds){
}
