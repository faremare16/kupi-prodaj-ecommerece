package com.faruk.backend.controller;

import com.faruk.backend.dto.CategoryResponseDto;
import com.faruk.backend.entity.Category;
import com.faruk.backend.service.CategoryService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories(){
        List<CategoryResponseDto> categoryResponseDtos = categoryService.getAllCategories();
        return ResponseEntity.ok().body(categoryResponseDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable Long id){
        CategoryResponseDto categoryResponseDto = categoryService.getCategoryById(id);
        return ResponseEntity.ok().body(categoryResponseDto);
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDto> saveCategory(@RequestBody Category category){
       CategoryResponseDto categoryResponseDto = categoryService.saveCategory(category);
       return ResponseEntity.ok().body(categoryResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable Long id){
        categoryService.deleteCategoryById(id);
        return ResponseEntity.ok().build();
    }
}
