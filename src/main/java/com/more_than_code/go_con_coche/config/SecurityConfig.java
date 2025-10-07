package com.more_than_code.go_con_coche.config;

import com.more_than_code.go_con_coche.auth.JwtAuthenticationFilter;
import com.more_than_code.go_con_coche.auth.services.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/api/auth/**","http://localhost:5173/**", "https://More-ThanCode.github.io/GoConcoche-Front/**").permitAll()
                                .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/swagger-ui/index.html", "/v3/api-docs/**", "/health", "/actuator/**").permitAll()

                                .requestMatchers(HttpMethod.GET, "/api/renter-profiles/me").hasRole("RENTER")
                                .requestMatchers(HttpMethod.GET, "/api/renter-profiles/{username}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/renter-profiles").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/renter-profiles").hasRole("RENTER")
                                .requestMatchers(HttpMethod.PUT, "/api/renter-profiles/**").hasRole("RENTER")
                                .requestMatchers(HttpMethod.DELETE, "/api/renter-profiles/me").hasRole("RENTER")
                                .requestMatchers(HttpMethod.DELETE, "/api/renter-profiles/{id}").hasRole("ADMIN")

                                .requestMatchers(HttpMethod.GET, "/api/roles", "/api/registered-users").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/roles").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/roles/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/roles/**").hasRole("ADMIN")

                                .requestMatchers(HttpMethod.GET, "/api/owner-profiles").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/owner-profiles").hasRole("OWNER")
                                .requestMatchers(HttpMethod.PUT, "/api/owner-profiles/**").hasRole("OWNER")
                                .requestMatchers(HttpMethod.DELETE, "/api/owner-profiles/me").hasRole("OWNER")
                                .requestMatchers(HttpMethod.DELETE, "/api/owner-profiles/{id}").hasRole("ADMIN")

                                .requestMatchers(HttpMethod.GET, "/api/vehicles/my").hasRole("OWNER")
                                .requestMatchers(HttpMethod.POST, "/api/vehicles").hasRole("OWNER")
                                .requestMatchers(HttpMethod.PUT, "/api/vehicles/**").hasRole("OWNER")
                                .requestMatchers(HttpMethod.DELETE, "/api/vehicles/**").hasRole("OWNER")

                                .requestMatchers(HttpMethod.POST, "/api/vehicle-rental-offers").hasRole("OWNER")
                                .requestMatchers(HttpMethod.PUT, "/api/vehicle-rental-offers/**").hasRole("OWNER")
                                .requestMatchers(HttpMethod.DELETE, "/api/vehicle-rental-offers/**").hasRole("OWNER")

                                .requestMatchers(HttpMethod.POST, "/api/vehicle-reservations").hasRole("RENTER")
                                .requestMatchers(HttpMethod.PUT, "/api/vehicle-reservations/**").hasRole("RENTER")
                                .requestMatchers(HttpMethod.DELETE, "/api/vehicle-reservations/**").hasRole("RENTER")

                                .anyRequest().authenticated())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(authenticationProvider());

        return http.build();
    }
}