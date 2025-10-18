package com.athletica.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "shipments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}
