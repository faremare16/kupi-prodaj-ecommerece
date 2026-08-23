package com.faruk.backend.controller;

import com.faruk.backend.dto.UserResponseDto;
import com.faruk.backend.entity.User;
import com.faruk.backend.repository.UserRepository;
import com.faruk.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')") // Samo admin moze pristupiti
public class AdminController {
    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDto>> getAllUsersForAdmin() {
        List<UserResponseDto> users=userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUserByAdmin(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok().build();
    }
}
