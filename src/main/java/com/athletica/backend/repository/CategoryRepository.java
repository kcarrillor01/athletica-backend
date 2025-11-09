package com.athletica.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.athletica.backend.model.Category;

public interface CategoryRepository extends JpaRepository<Category, String> {
  Optional<Category> findBySlug(String slug);

  boolean existsBySlug(String slug);
}
