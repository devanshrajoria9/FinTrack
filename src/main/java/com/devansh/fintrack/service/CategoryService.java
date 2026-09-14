package com.devansh.fintrack.service;

import com.devansh.fintrack.dto.request.CreateCategoryRequestDto;
import com.devansh.fintrack.dto.request.UpdateCategoryRequestDto;
import com.devansh.fintrack.dto.response.CategoryResponseDto;
import com.devansh.fintrack.entity.Category;
import com.devansh.fintrack.exception.ResourceNotFoundException;
import com.devansh.fintrack.repository.CategoryRepository;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository){
    this.categoryRepository = categoryRepository;
    }

    public CategoryResponseDto createCategory(CreateCategoryRequestDto request){
        Category category = mapToEntity(request);

        Category savedCategory = categoryRepository.save(category);

        return mapToDto(savedCategory);
    }

    public CategoryResponseDto getCategory(Long id ){
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category with id "+ id+ " not found"));

        return mapToDto(category);
    }

    public List<CategoryResponseDto> getAllCategories(){
        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .map(this :: mapToDto)
                .toList();
    }

    public CategoryResponseDto updateCategory(Long id, UpdateCategoryRequestDto updateCategory){
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() ->
                new ResourceNotFoundException("Category with id "+ id+ " not found"));

        existingCategory.setName(updateCategory.getName());
        existingCategory.setDescription(updateCategory.getDescription());
        existingCategory.setType(updateCategory.getType());

        Category savedCategory = categoryRepository.save(existingCategory);

        return mapToDto(savedCategory);
    }

    public void deleteCategory(Long id){
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category with id "+ id+ " not found"));

        categoryRepository.delete(category);
    }


    private Category mapToEntity(CreateCategoryRequestDto request){
        Category category = new Category();

        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setType(request.getType());

        return category;
    }

    private CategoryResponseDto mapToDto(Category category){
        CategoryResponseDto response = new CategoryResponseDto();

        response.setId(category.getId());
        response.setName(category.getName());
        response.setDescription(category.getDescription());
        response.setType(category.getType());
        response.setCreatedAt(category.getCreatedAt());
        response.setUpdatedAt(category.getUpdatedAt());

        return response;
    }
}