package com.athletica.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.athletica.backend.model.Shipment;

public interface ShipmentRepository extends JpaRepository<Shipment, String> {
  List<Shipment> findByOrderId(String orderId);

  Optional<Shipment> findFirstByOrderIdOrderByCreatedAtDesc(String orderId);
}
