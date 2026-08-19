package com.faruk.backend.service;

import com.faruk.backend.dto.ProductResponseDto;
import com.faruk.backend.entity.Category;
import com.faruk.backend.entity.Product;
import com.faruk.backend.entity.User;
import com.faruk.backend.repository.CategoryRepository;
import com.faruk.backend.repository.ProductRepository;
import com.faruk.backend.repository.UserRepository;
import lombok.Builder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final FileService fileService;
    private final String PRODUCT_UPLOAD_DIR="uploads/product_pictures/";

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository,
                          UserRepository userRepository,
                          FileService fileService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.fileService = fileService;
    }

    public List<ProductResponseDto> getAllProducts(){
        List<Product> products = productRepository.findAll();
        return mapProductsToDto(products);
    }

    public ProductResponseDto getProductResponseById(Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        String ownerUsername = "Unknown";
        if(product.getUser() != null) {
            ownerUsername = product.getUser().getUsername();
        }

        return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .unitsInStock(product.getUnitsInStock())
                .imageUrl(product.getImageUrl())
                .username(ownerUsername)
                .build();
    }

    public List<ProductResponseDto> getProductByUserId(Long userId){
        List<Product> products = productRepository.findByUserId(userId);
        return mapProductsToDto(products);
    }

    public Product createProduct(String name, Long categoryId, String description, BigDecimal price, Long unitsInStock, MultipartFile file, String currentUsername){
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id " + categoryId));

        User user= userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found with username " + currentUsername));

        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setUnitsInStock(unitsInStock);
        product.setCategory(category);
        product.setUser(user);

        // logika za snimanje slika u folder product_pictures
        if(file != null && !file.isEmpty()){
            try{
                File directory = new File(PRODUCT_UPLOAD_DIR);
                if(!directory.exists()){
                    directory.mkdirs();
                }

                String fileName = UUID.randomUUID().toString() + "." + file.getOriginalFilename();
                Path filePath = Paths.get(PRODUCT_UPLOAD_DIR + fileName);

                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                product.setImageUrl("product_pictures/"+fileName);
            }catch(IOException e){
                throw new RuntimeException("Error while trying to save product picture: " + e);
            }
        }

        if(product.getSku()==null || product.getSku().trim().isEmpty()){
            String uniqueSku = "SKU-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase();
            product.setSku(uniqueSku);
        }

        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + id));

        if (product.getImageUrl() != null) {
            fileService.deleteFile(product.getImageUrl());
        }

        productRepository.delete(product);
    }

    public List<ProductResponseDto> mapProductsToDto(List<Product> products){
        return products.stream().map(product -> {
            String ownerUsername = "Unknown";
            if (product.getUser() != null) {
                ownerUsername = product.getUser().getUsername();
            }

            return ProductResponseDto.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .description(product.getDescription())
                    .price(product.getPrice())
                    .unitsInStock(product.getUnitsInStock())
                    .imageUrl(product.getImageUrl())
                    .username(ownerUsername)
                    .build();
        }).collect(Collectors.toList());
    }

    public List<ProductResponseDto> searchProductByName(String name){
        List<Product> products=productRepository.findByNameContainingIgnoreCase(name);
        return mapProductsToDto(products);
    }

    public List<ProductResponseDto> searchProductByCategoryId(Long categoryId){
        List<Product> products=productRepository.findByCategoryId(categoryId);
        return mapProductsToDto(products);
    }

}
