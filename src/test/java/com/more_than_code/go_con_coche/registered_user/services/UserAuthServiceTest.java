package com.more_than_code.go_con_coche.registered_user.services;

import com.more_than_code.go_con_coche.auth.SecurityUser;
import com.more_than_code.go_con_coche.global.EntityNotFoundException;
import com.more_than_code.go_con_coche.registered_user.RegisteredUser;
import com.more_than_code.go_con_coche.registered_user.RegisteredUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import static org.mockito.Mockito.*;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserAuthServiceTest {
    @Mock
    private RegisteredUserRepository userRepository;

    @Mock
    private SecurityUser securityUser;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private UserAuthService authService;

    @BeforeEach
    void setUp() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(securityUser);
    }

    @Test
    void getAuthenticatedUser_success() {
        String username = "testUser";
        RegisteredUser expectedUser = new RegisteredUser();
        expectedUser.setUsername(username);

        when(securityUser.getUsername()).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(expectedUser));

        RegisteredUser actualUser = authService.getAuthenticatedUser();

        assertNotNull(actualUser);
        assertEquals(username, actualUser.getUsername());
    }

    @Test
    void getAuthenticatedUser_userNotFound() {
        String username = "missingUser";
        when(securityUser.getUsername()).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> authService.getAuthenticatedUser());

        assertTrue(exception.getMessage().contains("RegisteredUser"));
        assertTrue(exception.getMessage().contains(username));
    }
}