package com.devansh.fintrack.controller;

import com.devansh.fintrack.dto.request.CreateCategoryRequestDto;
import com.devansh.fintrack.dto.request.UpdateCategoryRequestDto;
import com.devansh.fintrack.dto.response.CategoryResponseDto;
import com.devansh.fintrack.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    @PostMapping
    public ResponseEntity<CategoryResponseDto> createCategory(
           @Valid @RequestBody CreateCategoryRequestDto request){
        CategoryResponseDto response = categoryService.createCategory(request);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getCategory(
            @PathVariable Long id){

        CategoryResponseDto category = categoryService.getCategory(id);

        return ResponseEntity.ok(category);
    }
    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories(){
        List<CategoryResponseDto> categories = categoryService.getAllCategories();

        return ResponseEntity.ok(categories);
    }
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> updateCategory(
            @PathVariable Long id, @Valid @RequestBody UpdateCategoryRequestDto request){
        CategoryResponseDto category = categoryService.updateCategory(id,request);

        return ResponseEntity.ok(category);
    }
    @DeleteMapping("/{id}")

    public ResponseEntity<Void> deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
