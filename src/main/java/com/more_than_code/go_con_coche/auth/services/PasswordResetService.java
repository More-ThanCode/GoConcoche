package com.more_than_code.go_con_coche.auth.services;

import com.more_than_code.go_con_coche.auth.dtos.ForgotPasswordRequest;
import com.more_than_code.go_con_coche.auth.dtos.ResetPasswordRequest;
import com.more_than_code.go_con_coche.email.EmailService;
import com.more_than_code.go_con_coche.global.EntityNotFoundException;
import com.more_than_code.go_con_coche.registered_user.RegisteredUser;
import com.more_than_code.go_con_coche.registered_user.RegisteredUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class PasswordResetService {
    private final RegisteredUserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, String> redisTemplate;

    private static final int EXPIRATION_MINUTES = 10;
    private static final String REDIS_PREFIX = "password_reset:";

    public void initiatePasswordReset(ForgotPasswordRequest request) {
        RegisteredUser user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new EntityNotFoundException(RegisteredUser.class.getSimpleName(), "email", request.email()));

        String token = UUID.randomUUID().toString();

        String redisKey = REDIS_PREFIX + token;
        redisTemplate.opsForValue().set(
                redisKey,
                user.getEmail(),
                EXPIRATION_MINUTES,
                TimeUnit.MINUTES
        );

        emailService.sendResetPasswordEmail(user.getEmail(), user.getFirstName(), token);
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        String redisKey = REDIS_PREFIX + request.token();

        String email = redisTemplate.opsForValue().get(redisKey);

        if (email == null) {
            throw new IllegalArgumentException("Invalid or expired reset token");
        }

        RegisteredUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);

        redisTemplate.delete(redisKey);
    }

    public boolean validateToken(String token) {
        String redisKey = REDIS_PREFIX + token;
        return Boolean.TRUE.equals(redisTemplate.hasKey(redisKey));
    }
}