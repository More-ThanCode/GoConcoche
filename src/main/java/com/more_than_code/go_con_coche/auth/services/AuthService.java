package com.more_than_code.go_con_coche.auth.services;

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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.more_than_code.go_con_coche.auth.exceptions.RefreshTokenCookiesNotFoundException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RegisteredUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

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

        userRepository.save(user);

        return new RegisterResponse(user.getId(), user.getUsername(), user.getRoles());
    }

    public AuthResponse authenticate(AuthRequest loginDto, HttpServletResponse response) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.username(), loginDto.password()));

        RegisteredUser user = userRepository.findByUsername(loginDto.username())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String jwtToken = jwtService.generateJwtToken(user.getUsername());
        String refreshToken = jwtService.generateRefreshToken(user.getUsername());
        Cookie refreshTokenCookie = getRefreshTokenCookie(refreshToken);
        response.addCookie(refreshTokenCookie);

        return new AuthResponse(user.getId(), user.getUsername(), jwtToken, user.getRoles());
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
}
