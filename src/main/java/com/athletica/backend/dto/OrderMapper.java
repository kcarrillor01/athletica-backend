package com.athletica.backend.dto;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.athletica.backend.model.Order;
import com.athletica.backend.model.OrderItem;

@Component
public class OrderMapper {

  public OrderDto toDto(Order order) {
    if (order == null)
      return null;

    OrderDto dto = new OrderDto();
    dto.setId(order.getId());
    dto.setUserId(order.getUser() != null ? order.getUser().getId() : null);
    dto.setAddressId(order.getAddress() != null ? order.getAddress().getId() : null);
    dto.setTotal(order.getTotal());
    dto.setCreatedAt(order.getCreatedAt());
    dto.setStatus(order.getStatus()); // <-- asignamos el enum directamente

    if (order.getItems() != null) {
      dto.setItems(order.getItems().stream().map(this::toItemDto).collect(Collectors.toList()));
    }
    
    return dto;
  }

  private OrderDto.OrderItemDto toItemDto(OrderItem item) {
    OrderDto.OrderItemDto dto = new OrderDto.OrderItemDto();
    dto.setProductId(item.getProduct() != null ? item.getProduct().getId() : null);
    dto.setProductName(item.getProduct() != null ? item.getProduct().getTitle() : null); // ojo: tu entidad usa title
    dto.setQuantity(item.getQuantity());
    dto.setPrice(item.getUnitPrice());
    
    return dto;
    
  }

  
}
