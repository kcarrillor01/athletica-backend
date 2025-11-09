package com.athletica.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.athletica.backend.dto.ApiResponse;
import com.athletica.backend.dto.CreateOrderRequest;
import com.athletica.backend.dto.OrderDto;
import com.athletica.backend.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  /**
   * Crear un nuevo pedido.
   * El cuerpo (CreateOrderRequest) puede contener userId o (mejor) el controlador
   * puede
   * aceptar userId desde el token. Aquí se delega tal como lo tenías.
   */
  @PostMapping
  public ResponseEntity<ApiResponse<OrderDto>> createOrder(@RequestBody CreateOrderRequest request) {
    OrderDto order = orderService.createOrder(request);
    return ResponseEntity.ok(new ApiResponse<>(200, "OK", order));
  }

  /**
   * Obtener todos los pedidos (solo ADMIN).
   */
  @GetMapping
  public ResponseEntity<ApiResponse<List<OrderDto>>> getAllOrders() {
    List<OrderDto> orders = orderService.getAllOrders(); // requiere implementación en OrderService
    return ResponseEntity.ok(new ApiResponse<>(200, "OK", orders));
  }

  /**
   * Obtener pedidos del usuario autenticado.
   */
  @GetMapping("/")
  public ResponseEntity<ApiResponse<List<OrderDto>>> getMyOrders(Authentication authentication) {
    if (authentication == null || authentication.getName() == null) {
      return ResponseEntity.status(401).body(new ApiResponse<>(401, "No autenticado", null));
    }
    String userId = authentication.getName();
    List<OrderDto> orders = orderService.getOrdersForUser(userId); // requiere implementación en OrderService
    return ResponseEntity.ok(new ApiResponse<>(200, "OK", orders));
  }
}
