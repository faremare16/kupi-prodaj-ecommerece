package com.faruk.backend.controller;

import com.faruk.backend.entity.Product;
import com.faruk.backend.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts(){
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getAllProductsById(@PathVariable Long id){
        return productService.getProductById(id);
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product){
        return productService.saveProduct(product);
    }

    @DeleteMapping
    public Product deleteProduct(@RequestBody Product product){
        return productService.saveProduct(product);
    }

    @DeleteMapping("/{id}")
    public void deleteProductById(@PathVariable Long id){
        productService.deleteProductById(id);
    }

    @GetMapping("/search/name")
    public List<Product> searchProductByName(@RequestParam String name){
        return productService.searchProductByName(name);
    }

    @GetMapping("/search/category")
    public List<Product> searchProductByCategoryId(@RequestParam Long categoryId){
        return productService.searchProductByCategoryId(categoryId);
    }
}
