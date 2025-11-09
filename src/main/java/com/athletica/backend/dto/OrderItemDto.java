package com.athletica.backend.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO usado cuando el cliente envía los items que desea comprar.
 * El servidor valida stock y calcula precios; por eso no incluimos unitPrice
 * obligatorio.
 */

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

  public OrderItemDto() {
  }

  public OrderItemDto(@NotBlank(message = "productId es requerido") String productId,
      @NotNull(message = "quantity es requerido") @Min(value = 1, message = "La cantidad mínima es 1") Integer quantity,
      BigDecimal unitPrice) {
    this.productId = productId;
    this.quantity = quantity;
    this.unitPrice = unitPrice;
  }

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public java.math.BigDecimal getUnitPrice() {
    return unitPrice;
  }

  public void setUnitPrice(java.math.BigDecimal unitPrice) {
    this.unitPrice = unitPrice;
  }

  
}
