package com.athletica.backend.service;

import java.util.List;

import com.athletica.backend.dto.CategoryDto;

public interface CategoryService {
  List<CategoryDto> getAllCategories();

  CategoryDto getCategory(String id);

  CategoryDto saveCategory(CategoryDto dto); // create or edit

  void deleteCategory(String id);
}
