package com.athletica.backend.repository;

import com.athletica.backend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
  Optional<Category> findBySlug(String slug);

  List<Category> findByNameContainingIgnoreCase(String namePart);

  boolean existsBySlug(String slug);
}
