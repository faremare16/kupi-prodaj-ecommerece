package com.faruk.backend.controller;

import com.faruk.backend.dto.UserResponseDto;
import com.faruk.backend.entity.User;
import com.faruk.backend.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users=userService.getAllUsers();
        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        UserResponseDto user=userService.getUsersResponseById(id);
        return ResponseEntity.ok().body(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponseDto> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok().body(new UserResponseDto());
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getCurrentUser(Authentication authentication) {
        String currentUsername=authentication.getName();
        UserResponseDto userProfile=userService.getUserProfile(currentUsername);
        return ResponseEntity.ok(userProfile);
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponseDto> updateUser(
            Authentication authentication,
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam(value="file", required = false) MultipartFile file) {

        String currentUsername=authentication.getName();

        if (file == null || file.isEmpty() || file.getOriginalFilename() == null || file.getOriginalFilename().trim().isEmpty()) {
            file = null;
        }

        System.out.println("PRIMLJEN FAJL NA BACKENDU: " + (file != null ? file.getOriginalFilename() + " (Veličina: " + file.getSize() + " bajtova)" : "FAJL JE NULL"));

        UserResponseDto updatedUser=userService.updateUser(currentUsername, username, email, phoneNumber, file);
        return ResponseEntity.ok(updatedUser);
    }
}
