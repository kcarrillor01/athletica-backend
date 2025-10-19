package com.athletica.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.athletica.backend.model.Order;

public interface OrderRepository extends JpaRepository<Order, String> {
  List<Order> findByUserId(String userId);
}
