package com.athletica.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.athletica.backend.dto.ApiResponse;
import com.athletica.backend.dto.CategoryDto;
import com.athletica.backend.service.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

  private final CategoryService categoryService;

  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<CategoryDto>>> getAll() {
    return ResponseEntity.ok(new ApiResponse<>(200, "OK", categoryService.getAllCategories()));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<CategoryDto>> getOne(@PathVariable String id) {
    return ResponseEntity.ok(new ApiResponse<>(200, "OK", categoryService.getCategory(id)));
  }

  /**
   * POST usado para create + update: si dto.id está presente -> update, si no ->
   * create
   */
  @PostMapping
  public ResponseEntity<ApiResponse<CategoryDto>> save(@RequestBody @Valid CategoryDto dto) {
    CategoryDto saved = categoryService.saveCategory(dto);
    // si se creó (dto.id == null) podrías devolver CREATED, pero para consistencia
    // devolvemos 200 OK
    return ResponseEntity.ok(new ApiResponse<>(200, "OK", saved));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<Object>> delete(@PathVariable String id) {
    categoryService.deleteCategory(id);
    return ResponseEntity.ok(new ApiResponse<>(200, "Deleted", null));
  }
}
