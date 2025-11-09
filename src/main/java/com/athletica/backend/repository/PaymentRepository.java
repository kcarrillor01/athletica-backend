package com.athletica.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.athletica.backend.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, String> {
  List<Payment> findByOrderId(String orderId);
}
