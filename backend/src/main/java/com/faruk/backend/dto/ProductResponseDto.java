package com.faruk.backend.dto;

import com.faruk.backend.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductResponseDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Long unitsInStock;
    private String imageUrl;
    private Date datePublished;
    private Category category;
    private UserResponseDto user;
}
