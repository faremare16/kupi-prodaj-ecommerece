package com.faruk.backend.controller;

import com.faruk.backend.dto.ProductResponseDto;
import com.faruk.backend.entity.Product;
import com.faruk.backend.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts(){
        List<ProductResponseDto> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductsById(@PathVariable Long id){
        ProductResponseDto products=productService.getProductResponseById(id);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ProductResponseDto>> getProductsByUserId(@PathVariable Long userId) {
        List<ProductResponseDto> products=productService.getProductByUserId(userId);
        return ResponseEntity.ok(products);
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(
            @RequestParam("name") String name,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("description") String description,
            @RequestParam("price") BigDecimal price,
            @RequestParam("unitsInStock") Long unitsInStock,
            @RequestParam(value="file", required = false ) MultipartFile file,
            Authentication authentication

    ) {
        String currentUsername=authentication.getName();
        Product createProduct = productService.createProduct(
                name, categoryId, description, price, unitsInStock, file, currentUsername
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(createProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search/name")
    public ResponseEntity<List<ProductResponseDto>> searchProductByName(@RequestParam String name){
        List<ProductResponseDto> products=productService.searchProductByName(name);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search/category")
    public ResponseEntity<List<ProductResponseDto>> searchProductByCategoryId(@RequestParam Long categoryId){
        List<ProductResponseDto> products=productService.searchProductByCategoryId(categoryId);
        return ResponseEntity.ok(products);
    }
}
