package com.devansh.fintrack.dto.request;

import com.devansh.fintrack.enums.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UpdateCategoryRequestDto {
    @NotBlank(message = "Category name is required")
    @Size(min = 3, max = 50, message = "Category name must be between 3 and 50 characters")
    private String name;
    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description;
    @NotNull(message = "Category type is required")
    private CategoryType type;

    public UpdateCategoryRequestDto(){

    }

    public UpdateCategoryRequestDto(String name, String description, CategoryType type) {
        this.name = name;
        this.description = description;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CategoryType getType() {
        return type;
    }

    public void setType(CategoryType type) {
        this.type = type;
    }
}
