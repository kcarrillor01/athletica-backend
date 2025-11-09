package com.athletica.backend.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.athletica.backend.dto.CategoryDto;
import com.athletica.backend.model.Category;
import com.athletica.backend.repository.CategoryRepository;
import com.athletica.backend.service.CategoryService;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepository categoryRepository;

  public CategoryServiceImpl(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  @Override
  public List<CategoryDto> getAllCategories() {
    return categoryRepository.findAll()
        .stream()
        .map(this::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public CategoryDto getCategory(String id) {
    return categoryRepository.findById(id)
        .map(this::toDto)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria no encontrada"));
  }

  @Override
  public CategoryDto saveCategory(CategoryDto dto) {
    if (dto == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cuerpo requerido");
    }
    if (dto.getName() == null || dto.getName().isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre es obligatorio");
    }
    if (dto.getSlug() == null || dto.getSlug().isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El slug es obligatorio");
    }

    boolean creating = (dto.getId() == null || dto.getId().isBlank());
    Category category;

    // check slug uniqueness
    if (creating) {
      if (categoryRepository.existsBySlug(dto.getSlug())) {
        throw new ResponseStatusException(HttpStatus.CONFLICT, "Slug ya existe");
      }
      category = new Category();
      category.setId(UUID.randomUUID().toString());
      category.setCreatedAt(LocalDateTime.now());
    } else {
      category = categoryRepository.findById(dto.getId())
          .orElseThrow(
              () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria no encontrada para actualizar"));

      // if slug changed, ensure uniqueness
      if (!dto.getSlug().equals(category.getSlug()) && categoryRepository.existsBySlug(dto.getSlug())) {
        throw new ResponseStatusException(HttpStatus.CONFLICT, "Slug ya existe");
      }
    }

    category.setName(dto.getName());
    category.setSlug(dto.getSlug());
    category.setDescription(dto.getDescription());

    Category saved = categoryRepository.save(category);
    return toDto(saved);
  }

  @Override
  public void deleteCategory(String id) {
    if (!categoryRepository.existsById(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria no encontrada");
    }
    categoryRepository.deleteById(id);
  }

  private CategoryDto toDto(Category c) {
    if (c == null)
      return null;
    return new CategoryDto(c.getId(), c.getName(), c.getSlug(), c.getDescription(), c.getCreatedAt());
  }
}
