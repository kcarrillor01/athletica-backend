package com.athletica.backend.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequestDto {
  private String addressId;
  @NotNull
  private List<@Valid OrderItemDto> items;
  // otros campos: shippingMethod, paymentMethod, etc.
}
