package com.athletica.backend.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "shipments")

public class Shipment {
  @Id
  @Column(length = 36)
  private String id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id", nullable = false, foreignKey = @ForeignKey(name = "fk_shipments_order"))
  private Order order;

  private String courier;

  @Column(name = "tracking_number")
  private String trackingNumber;

  @Enumerated(EnumType.STRING)
  private ShipmentStatus status;

  @Column(name = "shipped_at")
  private LocalDateTime shippedAt;

  @Column(name = "delivered_at")
  private LocalDateTime deliveredAt;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  public Shipment() {
  }

  public Shipment(String id, Order order, String courier, String trackingNumber, ShipmentStatus status,
      LocalDateTime shippedAt, LocalDateTime deliveredAt, LocalDateTime createdAt) {
    this.id = id;
    this.order = order;
    this.courier = courier;
    this.trackingNumber = trackingNumber;
    this.status = status;
    this.shippedAt = shippedAt;
    this.deliveredAt = deliveredAt;
    this.createdAt = createdAt;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  public String getCourier() {
    return courier;
  }

  public void setCourier(String courier) {
    this.courier = courier;
  }

  public String getTrackingNumber() {
    return trackingNumber;
  }

  public void setTrackingNumber(String trackingNumber) {
    this.trackingNumber = trackingNumber;
  }

  public ShipmentStatus getStatus() {
    return status;
  }

  public void setStatus(ShipmentStatus status) {
    this.status = status;
  }

  public LocalDateTime getShippedAt() {
    return shippedAt;
  }

  public void setShippedAt(LocalDateTime shippedAt) {
    this.shippedAt = shippedAt;
  }

  public LocalDateTime getDeliveredAt() {
    return deliveredAt;
  }

  public void setDeliveredAt(LocalDateTime deliveredAt) {
    this.deliveredAt = deliveredAt;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

}
