package com.athletica.backend.dto;

import java.util.List;

public class CreateOrderRequest {

  private String userId;
  private String addressId;
  private List<OrderItemRequest> items;

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

  public List<OrderItemRequest> getItems() {
    return items;
  }

  public void setItems(List<OrderItemRequest> items) {
    this.items = items;
  }

  public static class OrderItemRequest {
    private String productId;
    private int quantity;

    public String getProductId() {
      return productId;
    }

    public void setProductId(String productId) {
      this.productId = productId;
    }

    public int getQuantity() {
      return quantity;
    }

    public void setQuantity(int quantity) {
      this.quantity = quantity;
    }
  }
}
