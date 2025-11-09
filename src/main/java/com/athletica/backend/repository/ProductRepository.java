package com.athletica.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.athletica.backend.model.Product;

import jakarta.transaction.Transactional;

public interface ProductRepository extends JpaRepository<Product, String> {

  @Transactional
  @Modifying
  @Query("UPDATE Product p SET p.stock = p.stock - :quantity WHERE p.id = :productId AND p.stock >= :quantity")
  int decrementStock(String productId, int quantity);

  List<Product> findByActiveTrue();
}
