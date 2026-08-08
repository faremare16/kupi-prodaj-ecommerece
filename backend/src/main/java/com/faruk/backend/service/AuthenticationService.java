package com.faruk.backend.service;

import com.faruk.backend.dto.AuthenticationRequest;
import com.faruk.backend.dto.AuthenticationResponse;
import com.faruk.backend.dto.RegisterRequest;
import com.faruk.backend.entity.Role;
import com.faruk.backend.entity.User;
import com.faruk.backend.repository.RoleRepository;
import com.faruk.backend.repository.UserRepository;
import com.faruk.backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    // registracija
    public AuthenticationResponse register(RegisterRequest registerRequest) {
        // ako se password i confirm password ne poklapaju
        if(!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())){
            throw new RuntimeException("Passwords do not match");
        }

        Role role = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role Not Found"));

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        // kreira novog korisnika sa hesiranom lozinkom
        var user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .phoneNumber(registerRequest.getPhoneNumber())
                .dateCreated(new Date())
                .roles(roles)
                .build();
        userRepository.save(user);

        // kreira novi jwt token za tog korisnika
        var JwtToken = jwtService.generateToken(user);
        return new AuthenticationResponse(JwtToken);
    }

    public AuthenticationResponse login(AuthenticationRequest request) {

        // provjerava email i lozinku preko Authentication menager
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // ako je provjera uspjela nadji korisnika u bazi
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()->new RuntimeException("User not found"));

        var jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponse(jwtToken);
    }
}
