package com.faruk.backend.service;

import com.faruk.backend.dto.CategoryResponseDto;
import com.faruk.backend.dto.ProductResponseDto;
import com.faruk.backend.entity.Category;
import com.faruk.backend.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryResponseDto> getAllCategories(){
        List<Category> categories = categoryRepository.findAll();
        return mapCategoriesToDo(categories);

    }

    public CategoryResponseDto getCategoryById(Long id){
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return mapCategoryToDo(category);
    }

    public CategoryResponseDto saveCategory(Category category){
        Category savedCategory = categoryRepository.save(category);
        return mapCategoryToDo(savedCategory);
    }

    public void deleteCategoryById(Long id){
        if(!categoryRepository.existsById(id)){
            throw new RuntimeException("Category not found");
        }else{
            categoryRepository.deleteById(id);
        }
    }

    // pomocna metoda za mapiranje jedne kategorije
    public CategoryResponseDto mapCategoryToDo(Category category){
        return CategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .products(category.getProducts())
                .build();
    }
    public List<CategoryResponseDto> mapCategoriesToDo(List <Category> categories){
        return categories.stream()
                .map(this::mapCategoryToDo)
                .collect(Collectors.toList());
    }



}
