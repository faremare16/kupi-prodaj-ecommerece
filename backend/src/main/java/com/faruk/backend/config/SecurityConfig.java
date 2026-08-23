package com.faruk.backend.config;

import com.faruk.backend.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        // 1. Javne rute za auth i pretragu/pregled
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/products/**", "/api/v1/categories/**").permitAll()

                        // 2. Slanje slika / fajlova javno
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/product_pictures/**", "/uploads/**").permitAll()

                        // 3. Sve ostalo (POST, PUT, DELETE, /api/v1/users/me itd.) MORA biti prijavljeno
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer  webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/product_pictures/**", "/uploads/**");
    }
}
