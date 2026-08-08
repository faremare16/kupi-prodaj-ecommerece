package com.faruk.backend.service;

import com.faruk.backend.entity.Product;
import com.faruk.backend.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    // ubacivanje ProductRepositorya preko konstruktora
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public Product getProductById(Long id){
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Product saveProduct(Product product){
        return productRepository.save(product);
    }

    public void deleteProductById(Long id){
        if(!productRepository.existsById(id)){
            throw new RuntimeException("Product not found");
        }else{
            productRepository.deleteById(id);
        }
    }

    public List<Product> searchProductByName(String name){
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Product> searchProductByCategoryId(Long categoryId){
        return productRepository.findByCategoryId(categoryId);
    }
}
