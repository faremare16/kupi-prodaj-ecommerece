package com.faruk.backend.service;

import com.faruk.backend.dto.ProductResponseDto;
import com.faruk.backend.dto.UserResponseDto;
import com.faruk.backend.entity.Category;
import com.faruk.backend.entity.Product;
import com.faruk.backend.entity.User;
import com.faruk.backend.repository.CategoryRepository;
import com.faruk.backend.repository.ProductRepository;
import com.faruk.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ImgBbService imgBbService;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository,
                          UserRepository userRepository,
                          ImgBbService imgBbService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.imgBbService = imgBbService;
    }

    public List<ProductResponseDto> getAllProducts(){
        List<Product> products = productRepository.findAll();
        return mapProductsToDto(products);
    }

    public ProductResponseDto getProductResponseById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        UserResponseDto userResponseDto = null;
        if (product.getUser() != null) {
            User u = product.getUser();
            userResponseDto = UserResponseDto.builder()
                    .id(u.getId())
                    .username(u.getUsername())
                    .email(u.getEmail())
                    .phoneNumber(u.getPhoneNumber())
                    .dateCreated(u.getDateCreated())
                    .profileImageUrl(u.getProfileImageUrl())
                    .build();
        }

        return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .unitsInStock(product.getUnitsInStock())
                .imageUrl(product.getImageUrl())
                .user(userResponseDto)
                .category(product.getCategory())
                .datePublished(product.getDatePublished())
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

        // logika za snimanje slika na IMGBB
        if(file != null && !file.isEmpty()){
            String imageUrl = imgBbService.uploadImage(file);
            product.setImageUrl(imageUrl);
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

        productRepository.delete(product);
    }

    public List<ProductResponseDto> mapProductsToDto(List<Product> products){
        return products.stream().map(product -> {
            UserResponseDto userResponseDto = null;
            if (product.getUser() != null) {
                User u = product.getUser();
                userResponseDto = UserResponseDto.builder()
                        .id(u.getId())
                        .username(u.getUsername())
                        .email(u.getEmail())
                        .phoneNumber(u.getPhoneNumber())
                        .dateCreated(u.getDateCreated())
                        .profileImageUrl(u.getProfileImageUrl())
                        .build();
            }

            return ProductResponseDto.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .description(product.getDescription())
                    .price(product.getPrice())
                    .unitsInStock(product.getUnitsInStock())
                    .imageUrl(product.getImageUrl())
                    .user(userResponseDto)
                    .category(product.getCategory())
                    .datePublished(product.getDatePublished())
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
