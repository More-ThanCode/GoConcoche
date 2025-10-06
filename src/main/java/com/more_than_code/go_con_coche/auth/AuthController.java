package com.more_than_code.go_con_coche.auth;

import com.more_than_code.go_con_coche.auth.dtos.*;
import com.more_than_code.go_con_coche.auth.services.AuthService;
import com.more_than_code.go_con_coche.auth.services.PasswordResetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Operations related to authentication and authorization. Public endpoints (no authentication required).")
public class AuthController {
    private final AuthService authService;
    private final PasswordResetService passwordResetService;

    @Operation(summary = "Register a new user", description = "Registers a new user and returns registration details")
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest registerDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.register(registerDto));
    }


    @Operation(summary = "Authenticate a user", description = "Authenticates user credentials and returns an access token")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest loginDto,
                                                     HttpServletResponse response) {
        AuthResponse userResponse = authService.authenticate(loginDto, response);
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + userResponse.token())
                .body(userResponse);
    }

    @Operation(summary = "Refresh access token", description = "Refreshes JWT token using a valid refresh token")
    @PostMapping("/refresh")
    public ResponseEntity<Void> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = authService.refreshToken(request, response);
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + accessToken)
                .build();
    }

    @Operation(summary = "Request password reset", description = "Sends a password reset email to the user")
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        passwordResetService.initiatePasswordReset(request);
        return ResponseEntity.ok("Password reset email sent successfully");
    }

    @Operation(summary = "Reset password", description = "Resets user password using the token from email")
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        passwordResetService.resetPassword(request);
        return ResponseEntity.ok("Password reset successfully");
    }

    @Operation(summary = "Validate reset token", description = "Check if a password reset token is valid")
    @GetMapping("/validate-reset-token")
    public ResponseEntity<Boolean> validateResetToken(@RequestParam String token) {
        boolean isValid = passwordResetService.validateToken(token);
        return ResponseEntity.ok(isValid);
    }
}
