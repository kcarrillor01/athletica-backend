package com.athletica.backend.dto;

import java.util.Collections;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.athletica.backend.model.Order;
import com.athletica.backend.model.OrderItem;
import com.athletica.backend.model.Product;

@Component
public class OrderMapper {

  public OrderDto toDto(Order order) {
    if (order == null)
      return null;

    OrderDto dto = new OrderDto();
    dto.setId(order.getId());

    if (order.getUser() != null) {
      dto.setUserId(order.getUser().getId());
    }

    if (order.getAddress() != null) {
      dto.setAddressId(order.getAddress().getId());
    }

    dto.setTotal(order.getTotal());
    dto.setCreatedAt(order.getCreatedAt());

    if (order.getItems() != null) {
      dto.setItems(order.getItems().stream().map(this::toItemDto).collect(Collectors.toList()));
    } else {
      dto.setItems(Collections.emptyList());
    }

    return dto;
  }

  private OrderDto.OrderItemDto toItemDto(OrderItem item) {
    OrderDto.OrderItemDto oi = new OrderDto.OrderItemDto();

    if (item == null)
      return oi;

    if (item.getProduct() != null) {
      Product p = item.getProduct();
      oi.setProductId(p.getId());
      // el nombre del producto en tu entidad es `title`
      oi.setProductName(p.getTitle());
    }

    oi.setQuantity(item.getQuantity() == null ? 0 : item.getQuantity());
    // en tu DTO el campo se llama `price` (usamos unitPrice del OrderItem si
    // existe)
    oi.setPrice(item.getUnitPrice() != null ? item.getUnitPrice()
        : (item.getProduct() != null ? item.getProduct().getPrice() : null));

    return oi;
  }
}
