package com.athletica.backend.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponseDto {
  private String id; // id del order_item (si ya existe)
  private String productId;
  private String productTitle; // opcional, para mostrar en la UI
  private Integer quantity;
  private BigDecimal unitPrice;
  private BigDecimal lineTotal; // unitPrice * quantity (calculado por el servidor)
}
