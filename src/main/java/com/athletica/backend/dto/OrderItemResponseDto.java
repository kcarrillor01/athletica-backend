package com.athletica.backend.dto;

import java.math.BigDecimal;

public class OrderItemResponseDto {
  private String id; // id del order_item (si ya existe)
  private String productId;
  private String productTitle; // opcional, para mostrar en la UI
  private Integer quantity;
  private BigDecimal unitPrice;
  private BigDecimal lineTotal; // unitPrice * quantity (calculado por el servidor)

  public OrderItemResponseDto() {
  }

  public OrderItemResponseDto(String id, String productId, String productTitle, Integer quantity, BigDecimal unitPrice,
      BigDecimal lineTotal) {
    this.id = id;
    this.productId = productId;
    this.productTitle = productTitle;
    this.quantity = quantity;
    this.unitPrice = unitPrice;
    this.lineTotal = lineTotal;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public String getProductTitle() {
    return productTitle;
  }

  public void setProductTitle(String productTitle) {
    this.productTitle = productTitle;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public BigDecimal getUnitPrice() {
    return unitPrice;
  }

  public void setUnitPrice(BigDecimal unitPrice) {
    this.unitPrice = unitPrice;
  }

  public BigDecimal getLineTotal() {
    return lineTotal;
  }

  public void setLineTotal(BigDecimal lineTotal) {
    this.lineTotal = lineTotal;
  }

}
