package com.athletica.backend.service;

import java.util.List;

import com.athletica.backend.dto.CreateShipmentRequest;
import com.athletica.backend.dto.ShipmentDto;
import com.athletica.backend.dto.UpdateShipmentRequest;

public interface ShipmentService {
  ShipmentDto createShipment(CreateShipmentRequest req, String actorUserId, boolean actorIsAdmin);

  ShipmentDto updateShipment(String id, UpdateShipmentRequest req, String actorUserId, boolean actorIsAdmin);

  void deleteShipment(String id, String actorUserId, boolean actorIsAdmin);

  ShipmentDto getShipmentById(String id, String actorUserId, boolean actorIsAdmin);

  List<ShipmentDto> getAllShipments(); // admin

  List<ShipmentDto> getShipmentsForOrder(String orderId, String actorUserId, boolean actorIsAdmin);
}
