package com.faruk.backend.dto;

import com.faruk.backend.entity.Product;
import com.faruk.backend.entity.Role;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private Long id;
    private String username;
    private String email;
    private String phoneNumber;
    private Date dateCreated;
    private String profileImageUrl;
    private List<Product> products;
    private Set<Role> roles = new HashSet<>();
}
