package com.devansh.fintrack.service;

import com.devansh.fintrack.dto.request.CreateCategoryRequestDto;
import com.devansh.fintrack.dto.request.UpdateCategoryRequestDto;
import com.devansh.fintrack.dto.response.CategoryResponseDto;
import com.devansh.fintrack.entity.Category;
import com.devansh.fintrack.entity.User;
import com.devansh.fintrack.exception.ResourceNotFoundException;
import com.devansh.fintrack.repository.CategoryRepository;
import com.devansh.fintrack.repository.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public CategoryService(CategoryRepository categoryRepository, UserRepository userRepository){
    this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public CategoryResponseDto createCategory(CreateCategoryRequestDto request){

        User currentuser = getCurrentUser();

        Category category = mapToEntity(request);
        category.setUser(currentuser);

        Category savedCategory = categoryRepository.save(category);

        return mapToDto(savedCategory);
    }

    public CategoryResponseDto getCategory(Long id ){

        User currentUser = getCurrentUser();

        Category category = categoryRepository.findByIdAndUserId(id, currentUser.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category with id "+ id+ " not found"));

        return mapToDto(category);
    }

    public List<CategoryResponseDto> getAllCategories(){

        User currentUser = getCurrentUser();

        List<Category> categories = categoryRepository.findAllByUserId(currentUser.getId());

        return categories.stream()
                .map(this :: mapToDto)
                .toList();
    }

    public CategoryResponseDto updateCategory(Long id, UpdateCategoryRequestDto updateCategory){

        User currentUser = getCurrentUser();

        Category existingCategory = categoryRepository.findByIdAndUserId(id, currentUser.getId())
                .orElseThrow(() ->
                new ResourceNotFoundException("Category with id "+ id+ " not found"));

        existingCategory.setName(updateCategory.getName());
        existingCategory.setDescription(updateCategory.getDescription());
        existingCategory.setType(updateCategory.getType());

        Category savedCategory = categoryRepository.save(existingCategory);

        return mapToDto(savedCategory);
    }

    public void deleteCategory(Long id){

        User currentUser = getCurrentUser();

        Category category = categoryRepository.findByIdAndUserId(id, currentUser.getId())
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

    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));

    }
}