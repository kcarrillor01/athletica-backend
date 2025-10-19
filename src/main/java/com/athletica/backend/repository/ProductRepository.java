package com.athletica.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.athletica.backend.model.Product;

public interface ProductRepository extends JpaRepository<Product, String> {
  List<Product> findByActiveTrue();
}
