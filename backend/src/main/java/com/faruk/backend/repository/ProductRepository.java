package com.faruk.backend.repository;

import com.faruk.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingIgnoreCase(String name); // pronađe proizvode koji sadrze ovaj dio teksta

    List<Product> findByCategoryId(Long id);

    List<Product> findByUserId(Long userId);
}
