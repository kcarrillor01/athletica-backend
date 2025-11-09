package com.athletica.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDto {
  private String id;
  private String userId;
  private String addressId;
  private BigDecimal total;
  private LocalDateTime createdAt;
  private List<OrderItemDto> items;

  public static class OrderItemDto {
    private String productId;
    private String productName;
    private int quantity;
    private BigDecimal price;

    // Getters y setters
    public String getProductId() {
      return productId;
    }

    public void setProductId(String productId) {
      this.productId = productId;
    }

    public String getProductName() {
      return productName;
    }

    public void setProductName(String productName) {
      this.productName = productName;
    }

    public int getQuantity() {
      return quantity;
    }

    public void setQuantity(int quantity) {
      this.quantity = quantity;
    }

    public BigDecimal getPrice() {
      return price;
    }

    public void setPrice(BigDecimal price) {
      this.price = price;
    }
  }

  // Getters y setters
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getAddressId() {
    return addressId;
  }

  public void setAddressId(String addressId) {
    this.addressId = addressId;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public List<OrderItemDto> getItems() {
    return items;
  }

  public void setItems(List<OrderItemDto> items) {
    this.items = items;
  }
}
