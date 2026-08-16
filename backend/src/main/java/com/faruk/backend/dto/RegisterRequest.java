package com.faruk.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String username;

    @Email(message="Format of email isn't suppored")
    private String email;

    @Size(min=6, message="Password needs to have atleast 6 characters")
    private String password;

    private String confirmPassword;
    private String phoneNumber;
}
