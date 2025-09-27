package com.more_than_code.go_con_coche.auth.services;

import com.more_than_code.go_con_coche.auth.dtos.AuthRequest;
import com.more_than_code.go_con_coche.auth.dtos.AuthResponse;
import com.more_than_code.go_con_coche.auth.dtos.RegisterRequest;
import com.more_than_code.go_con_coche.auth.dtos.RegisterResponse;
import com.more_than_code.go_con_coche.auth.exceptions.RefreshTokenCookiesNotFoundException;
import com.more_than_code.go_con_coche.email.EmailService;
import com.more_than_code.go_con_coche.registered_user.RegisteredUser;
import com.more_than_code.go_con_coche.registered_user.RegisteredUserRepository;
import com.more_than_code.go_con_coche.role.Role;
import com.more_than_code.go_con_coche.role.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private RegisteredUserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpServletRequest request;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private AuthService authService;

    private String userName;
    private Role userRole;
    private  String password;
    private AuthRequest loginDto;

    @BeforeEach
    void setUp(){
        userName = "tomblack";
        userRole = Role.builder().id(1L).role("RENTER").build();
        password = "password123";
        loginDto = new AuthRequest(userName, password);
    }

    @Test
    void register_WhenValidRequest() {
        RegisterRequest registerRequest = new RegisterRequest("Tom",
                "Black",
                LocalDate.of(1990, 1, 1),
                "1234567890", userName,
                "tomblack@example.com",
                password,
                Set.of(1L));

        when(roleRepository.findById(1L)).thenReturn(Optional.of(userRole));
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
        RegisteredUser savedUser = RegisteredUser.builder()
                .id(10L).username(userName).roles(Set.of(userRole)).build();
        when(userRepository.save(any())).thenReturn(savedUser);

        RegisterResponse response = authService.register(registerRequest);

        assertEquals(userName, response.username());
        assertTrue(response.roles().contains(userRole.getRole()));
        verify(userRepository).save(any(RegisteredUser.class));
    }

    @Test
    void register_WhenAdminRoleRequested() {
        userRole.setRole("ADMIN");
        RegisterRequest registerRequest = new RegisterRequest(
                "Tom", "Black", LocalDate.of(1995, 1, 1),
                "9876543210", userName, "tomblack@example.com", password,
                Set.of(2L));

        when(roleRepository.findById(2L)).thenReturn(Optional.of(userRole));

        assertThrows(IllegalArgumentException.class, () -> authService.register(registerRequest));
    }

    @Test
    void authenticate_ShouldReturnAuthResponse_WhenValidCredentials() {
        RegisteredUser user = RegisteredUser.builder()
                .id(5L).username(userName)
                .roles(Set.of(userRole))
                .build();

        when(userRepository.findByUsername(userName)).thenReturn(Optional.of(user));
        when(jwtService.generateJwtToken(userName)).thenReturn("jwt-token");
        when(jwtService.generateRefreshToken(userName)).thenReturn("refresh-token");
        when(jwtService.extractExpiration("refresh-token")).thenReturn(new Date(System.currentTimeMillis() + 60000));

        AuthResponse authResponse = authService.authenticate(loginDto, response);

        assertEquals(userName, authResponse.username());
        assertEquals("jwt-token", authResponse.token());
        verify(response).addCookie(any(Cookie.class));
    }

    @Test
    void authenticate_ShouldThrow_WhenUserNotFound() {
        when(userRepository.findByUsername(userName)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> authService.authenticate(loginDto, response));
    }

    @Test
    void refreshToken_ShouldReturnNewJwt_WhenValidCookie() {
        Cookie oldRefreshCookie = new Cookie("refresh_token", "old-refresh");
        when(request.getCookies()).thenReturn(new Cookie[]{oldRefreshCookie});
        when(jwtService.extractSubject("old-refresh")).thenReturn(userName);
        when(jwtService.generateJwtToken(userName)).thenReturn("new-jwt");
        when(jwtService.generateRefreshToken(userName)).thenReturn("new-refresh");
        when(jwtService.extractExpiration("new-refresh")).thenReturn(new Date(System.currentTimeMillis() + 60000));

        String token = authService.refreshToken(request, response);

        assertEquals("new-jwt", token);
        verify(response).addCookie(any(Cookie.class));
    }

    @Test
    void refreshToken_ShouldThrow_WhenNoCookies() {
        when(request.getCookies()).thenReturn(null);

        assertThrows(RuntimeException.class, () -> authService.refreshToken(request, response));
    }

    @Test
    void refreshToken_WhenRefreshTokenCookieMissing() {
        Cookie otherCookie = new Cookie("some_cookie", "value");
        when(request.getCookies()).thenReturn(new Cookie[]{otherCookie});

        assertThrows(RefreshTokenCookiesNotFoundException.class,
                () -> authService.refreshToken(request, response));
    }
}