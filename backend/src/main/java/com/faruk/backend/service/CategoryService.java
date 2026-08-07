package com.faruk.backend.service;

import com.faruk.backend.entity.Category;
import com.faruk.backend.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id){
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public Category saveCategory(Category category){
        return categoryRepository.save(category);
    }

    public void deleteCategoryById(Long id){
        if(!categoryRepository.existsById(id)){
            throw new RuntimeException("Category not found");
        }else{
            categoryRepository.deleteById(id);
        }
    }



}
