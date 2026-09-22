package com.bookflow.security.config;

import com.bookflow.security.jwt.JwtAccessDeniedHandler;
import com.bookflow.security.jwt.JwtAuthenticationEntryPoint;
import com.bookflow.security.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            JwtAccessDeniedHandler jwtAccessDeniedHandler) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(
                                jwtAuthenticationEntryPoint
                        )
                        .accessDeniedHandler(
                                jwtAccessDeniedHandler
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // Public authentication endpoints
                        .requestMatchers(
                                "/api/auth/register",
                                "/api/auth/login"
                        ).permitAll()

                        // Public hotel endpoints
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/hotels/**"
                        ).permitAll()

                        // Hotel management - ADMIN only
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/hotels/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/hotels/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/hotels/**"
                        ).hasRole("ADMIN")

                        // Public room endpoints
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/rooms/**"
                        ).permitAll()

                        // Room management - ADMIN only
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/rooms/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/rooms/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/rooms/**"
                        ).hasRole("ADMIN")

                        // User management - ADMIN only
                        .requestMatchers("/api/users/**")
                        .hasRole("ADMIN")

                        // View all bookings - ADMIN only
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/bookings"
                        ).hasRole("ADMIN")

                        // Individual booking access - authenticated user
                        // Ownership will be enforced in Phase 11
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/bookings/*"
                        ).authenticated()

                        // Create booking - authenticated user
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/bookings"
                        ).authenticated()

                        // Update booking - authenticated user
                        // Ownership will be enforced in Phase 11
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/bookings/*"
                        ).authenticated()

                        // Delete/cancel booking - authenticated user
                        // Ownership will be enforced in Phase 11
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/bookings/*"
                        ).authenticated()

                        // Future dedicated admin endpoints
                        .requestMatchers("/api/admin/**")
                        .hasRole("ADMIN")

                        // All remaining endpoints require authentication
                        .anyRequest()
                        .authenticated()
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}