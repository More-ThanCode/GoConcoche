package com.more_than_code.go_con_coche.role.controller;

import com.more_than_code.go_con_coche.registered_user.RegisteredUser;
import com.more_than_code.go_con_coche.registered_user.RegisteredUserRepository;
import com.more_than_code.go_con_coche.role.Role;
import com.more_than_code.go_con_coche.role.RoleRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/me/roles")
@RequiredArgsConstructor
@Tag(name = "User Roles", description = "Endpoints for users to manage their own roles (owner and renter only).")
public class UserRoleController {

    private final RegisteredUserRepository userRepository;
    private final RoleRepository roleRepository;

    @Operation(summary = "Add a role to the current user", description = "Allows an authenticated user to add the role ROLE_OWNER or ROLE_RENTER to their account")
    @PostMapping("/{roleName}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> addRoleToMe(
            @Parameter(description = "Role to add (must be OWNER or RENTER)", example = "OWNER")
            @PathVariable String roleName,
            Authentication authentication
    ) {
        if (!roleName.equalsIgnoreCase("OWNER") && !roleName.equalsIgnoreCase("RENTER")) {
            return ResponseEntity.badRequest().body("Only OWNER or RENTER can be added");
        }

        RegisteredUser user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Role role = roleRepository.findByRoleIgnoreCase(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found in database"));

        if (user.getRoles().contains(role)) {
            return ResponseEntity.badRequest().body("User already has this role");
        }

        user.getRoles().add(role);
        userRepository.save(user);

        return ResponseEntity.ok("Role added successfully: " + roleName);
    }

    @Operation(summary = "Remove a role from the current user", description = "Allows an authenticated user to remove the role OWNER or RENTER from their account")
    @DeleteMapping("/{roleName}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> removeRoleFromMe(
            @Parameter(description = "Role to remove (must be OWNER or RENTER)", example = "RENTER")
            @PathVariable String roleName,
            Authentication authentication
    ) {
        if (!roleName.equalsIgnoreCase("OWNER") && !roleName.equalsIgnoreCase("RENTER")) {
            return ResponseEntity.badRequest().body("Only OWNER or RENTER can be removed");
        }

        RegisteredUser user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Role role = roleRepository.findByRoleIgnoreCase(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found in database"));

        if (!user.getRoles().contains(role)) {
            return ResponseEntity.badRequest().body("User doesn't have this role");
        }

        user.getRoles().remove(role);
        userRepository.save(user);

        return ResponseEntity.ok("Role removed successfully: " + roleName);
    }
}

