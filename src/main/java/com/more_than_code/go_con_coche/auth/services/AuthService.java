package com.more_than_code.go_con_coche.auth.services;

import com.more_than_code.go_con_coche.email.EmailService;
import com.more_than_code.go_con_coche.role.Role;
import com.more_than_code.go_con_coche.role.RoleRepository;
import com.more_than_code.go_con_coche.auth.dtos.AuthRequest;
import com.more_than_code.go_con_coche.auth.dtos.AuthResponse;
import com.more_than_code.go_con_coche.auth.dtos.RegisterRequest;
import com.more_than_code.go_con_coche.auth.dtos.RegisterResponse;
import com.more_than_code.go_con_coche.registered_user.RegisteredUser;
import com.more_than_code.go_con_coche.registered_user.RegisteredUserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.more_than_code.go_con_coche.auth.exceptions.RefreshTokenCookiesNotFoundException;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RegisteredUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final EmailService emailService;

    public RegisterResponse register(RegisterRequest registerDto) {
        Set<Role> roles = registerDto.roleIds().stream()
                .map(roleId -> roleRepository.findById(roleId)
                        .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleId)))
                .collect(Collectors.toSet());

        if (roles.stream().anyMatch(r -> r.getRole().equals("ADMIN"))) {
            throw new IllegalArgumentException("Cannot register with ADMIN role");
        }

        RegisteredUser user = RegisteredUser.builder()
                .firstName(registerDto.firstName())
                .lastName(registerDto.lastName())
                .dateOfBirth(registerDto.dateOfBirth())
                .phoneNumber(registerDto.phoneNumber())
                .username(registerDto.username())
                .email(registerDto.email())
                .password(passwordEncoder.encode(registerDto.password()))
                .roles(roles)
                .build();
        RegisteredUser savedUser = userRepository.save(user);
        Set<String> roleNames = getRoleNames(savedUser);
        emailService.sendRegistrationEmail(user.getEmail(), user.getFirstName());
        return new RegisterResponse(savedUser.getUsername(), roleNames);
    }

    public AuthResponse authenticate(AuthRequest loginDto, HttpServletResponse response) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.username(), loginDto.password()));

        RegisteredUser user = userRepository.findByUsername(loginDto.username())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String jwtToken = jwtService.generateJwtToken(user.getUsername());
        String refreshToken = jwtService.generateRefreshToken(user.getUsername());
        Cookie refreshTokenCookie = getRefreshTokenCookie(refreshToken);
        response.addCookie(refreshTokenCookie);
        Set<String> roleNames = getRoleNames(user);

        return new AuthResponse(user.getUsername(), jwtToken, roleNames);
    }

    public String refreshToken(HttpServletRequest request, HttpServletResponse response) {
        validateCookies(request);
        Cookie refreshTokenCookie = Arrays.stream(request.getCookies())
                .filter(cookie -> "refresh_token".equals(cookie.getName()))
                .findFirst()
                .orElseThrow(RefreshTokenCookiesNotFoundException::new);
        String currentRefreshToken = refreshTokenCookie.getValue();

        String username = jwtService.extractSubject(currentRefreshToken);
        String newJwtToken = jwtService.generateJwtToken(username);
        String newRefreshToken = jwtService.generateRefreshToken(username);
        Cookie newRefreshCookie = getRefreshTokenCookie(newRefreshToken);
        response.addCookie(newRefreshCookie);

        return newJwtToken;
    }

    private Cookie getRefreshTokenCookie(String token) {
        long expirationMillis = jwtService.extractExpiration(token).getTime();
        long remainingSeconds = (expirationMillis - System.currentTimeMillis()) / 1000;
        int cookieMaxAge = remainingSeconds > 0 ? (int) remainingSeconds : 0;

        Cookie refreshTokenCookie = new Cookie("refresh_token", token);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(cookieMaxAge);

        return refreshTokenCookie;
    }

    private void validateCookies(HttpServletRequest request) {
        if (request.getCookies() == null) {
            throw new RuntimeException("No cookies present");
        }
    }

    private Set<String> getRoleNames(RegisteredUser user){
        return user.getRoles().stream()
                .map(Role::getRole)
                .collect(Collectors.toSet());
    }
}
