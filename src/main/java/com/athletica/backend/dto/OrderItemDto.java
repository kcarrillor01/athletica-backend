package com.athletica.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO usado cuando el cliente envía los items que desea comprar.
 * El servidor valida stock y calcula precios; por eso no incluimos unitPrice
 * obligatorio.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDto {

  @NotBlank(message = "productId es requerido")
  private String productId;

  @NotNull(message = "quantity es requerido")
  @Min(value = 1, message = "La cantidad mínima es 1")
  private Integer quantity;

  /**
   * Opcional: el frontend puede enviar el precio mostrado para
   * verificación/optimización,
   * pero el servidor debería ignorarlo al calcular el total real (evitar confiar
   * en el cliente).
   */
  private java.math.BigDecimal unitPrice;
}
