package com.athletica.backend.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;









public class OrderRequestDto {
  private String addressId;
  @NotNull
  private List<@Valid OrderItemDto> items;
  // otros campos: shippingMethod, paymentMethod, etc.
}
