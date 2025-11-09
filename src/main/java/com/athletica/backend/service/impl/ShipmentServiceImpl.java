package com.athletica.backend.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.athletica.backend.dto.CreateShipmentRequest;
import com.athletica.backend.dto.ShipmentDto;
import com.athletica.backend.dto.UpdateShipmentRequest;
import com.athletica.backend.model.Order;
import com.athletica.backend.model.Shipment;
import com.athletica.backend.model.ShipmentStatus;
import com.athletica.backend.repository.OrderRepository;
import com.athletica.backend.repository.ShipmentRepository;
import com.athletica.backend.service.ShipmentService;

@Service
public class ShipmentServiceImpl implements ShipmentService {

  private final ShipmentRepository shipmentRepository;
  private final OrderRepository orderRepository;

  public ShipmentServiceImpl(ShipmentRepository shipmentRepository, OrderRepository orderRepository) {
    this.shipmentRepository = shipmentRepository;
    this.orderRepository = orderRepository;
  }

  @Override
  @Transactional
  public ShipmentDto createShipment(CreateShipmentRequest req, String actorUserId, boolean actorIsAdmin) {
    if (req.getOrderId() == null || req.getOrderId().isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "orderId es requerido");
    }

    Order order = orderRepository.findById(req.getOrderId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order not found"));

    // Permisos: solo admin o el propietario del pedido pueden crear (si lo
    // permites)
    if (!actorIsAdmin && !actorUserId.equals(order.getUser().getId())) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No autorizado");
    }

    Shipment s = new Shipment();
    s.setId(UUID.randomUUID().toString());
    s.setOrder(order);
    s.setCourier(req.getCourier());
    s.setTrackingNumber(req.getTrackingNumber());
    s.setStatus(req.getStatus() != null ? ShipmentStatus.valueOf(req.getStatus()) : ShipmentStatus.PENDING);
    s.setShippedAt(req.getShippedAt());
    s.setDeliveredAt(req.getDeliveredAt());
    s.setCreatedAt(LocalDateTime.now());

    Shipment saved = shipmentRepository.save(s);

    // asegurar la relación en memoria si order tiene collection
    try {
      if (order.getShipments() != null) {
        order.getShipments().add(saved);
      }
    } catch (Exception e) {
      // ignore if not bidirectional
    }

    return toDto(saved);
  }

  @Override
  @Transactional
  public ShipmentDto updateShipment(String id, UpdateShipmentRequest req, String actorUserId, boolean actorIsAdmin) {
    Shipment s = shipmentRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shipment not found"));

    Order order = s.getOrder();
    if (!actorIsAdmin && !actorUserId.equals(order.getUser().getId())) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No autorizado");
    }

    if (req.getCourier() != null)
      s.setCourier(req.getCourier());
    if (req.getTrackingNumber() != null)
      s.setTrackingNumber(req.getTrackingNumber());
    if (req.getStatus() != null)
      s.setStatus(ShipmentStatus.valueOf(req.getStatus()));
    if (req.getShippedAt() != null)
      s.setShippedAt(req.getShippedAt());
    if (req.getDeliveredAt() != null)
      s.setDeliveredAt(req.getDeliveredAt());

    Shipment updated = shipmentRepository.save(s);
    return toDto(updated);
  }

  @Override
  @Transactional
  public void deleteShipment(String id, String actorUserId, boolean actorIsAdmin) {
    Shipment s = shipmentRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shipment not found"));

    if (!actorIsAdmin && !actorUserId.equals(s.getOrder().getUser().getId())) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No autorizado");
    }

    shipmentRepository.deleteById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public ShipmentDto getShipmentById(String id, String actorUserId, boolean actorIsAdmin) {
    Shipment s = shipmentRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shipment not found"));

    if (!actorIsAdmin && !actorUserId.equals(s.getOrder().getUser().getId())) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No autorizado");
    }

    return toDto(s);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ShipmentDto> getAllShipments() {
    return shipmentRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<ShipmentDto> getShipmentsForOrder(String orderId, String actorUserId, boolean actorIsAdmin) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

    if (!actorIsAdmin && !actorUserId.equals(order.getUser().getId())) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No autorizado");
    }

    return shipmentRepository.findByOrderId(orderId).stream().map(this::toDto).collect(Collectors.toList());
  }

  private ShipmentDto toDto(Shipment s) {
    ShipmentDto dto = new ShipmentDto();
    dto.setId(s.getId());
    dto.setOrderId(s.getOrder() != null ? s.getOrder().getId() : null);
    dto.setCourier(s.getCourier());
    dto.setTrackingNumber(s.getTrackingNumber());
    dto.setStatus(s.getStatus());
    dto.setShippedAt(s.getShippedAt());
    dto.setDeliveredAt(s.getDeliveredAt());
    dto.setCreatedAt(s.getCreatedAt());
    return dto;
  }
}
