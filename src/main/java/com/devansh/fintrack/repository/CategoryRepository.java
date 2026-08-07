package com.devansh.fintrack.repository;

import com.devansh.fintrack.entity.Category;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
