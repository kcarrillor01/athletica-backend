package com.athletica.backend.service;

import java.util.List;

import com.athletica.backend.dto.CreateOrderRequest;
import com.athletica.backend.dto.OrderDto;



public interface OrderService {
  OrderDto createOrder(CreateOrderRequest request);

  List<OrderDto> getAllOrders();

  List<OrderDto> getOrdersForUser(String userId);

}
