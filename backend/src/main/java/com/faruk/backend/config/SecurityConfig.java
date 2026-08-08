package com.faruk.backend.config;

import com.faruk.backend.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // iskljucujemo CSRF zastitu jer se koristi jwt token
                .csrf(csrf -> csrf.disable())

                // konfigurisemo pravila pristupa ruti
                .authorizeHttpRequests(auth -> auth

                        // definise sve rute koje su javne
                        .requestMatchers("/api/v1/auth/**").permitAll()

                        // sve ostale moraju biti zakljucane i traziti token
                        .anyRequest().authenticated()
                )

                // nema cuvanja sesija na serveru sve je preko tokena
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authenticationProvider(authenticationProvider)

                // ubacujemo nas jwt authentication filter prije spring boot filtera
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
