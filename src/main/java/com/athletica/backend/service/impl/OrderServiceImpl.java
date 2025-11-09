package com.athletica.backend.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.athletica.backend.dto.CreateOrderRequest;
import com.athletica.backend.dto.OrderDto;
import com.athletica.backend.dto.OrderMapper;
import com.athletica.backend.model.Address;
import com.athletica.backend.model.Order;
import com.athletica.backend.model.OrderItem;
import com.athletica.backend.model.OrderStatus;
import com.athletica.backend.model.Product;
import com.athletica.backend.model.User;
import com.athletica.backend.repository.AddressRepository;
import com.athletica.backend.repository.OrderRepository;
import com.athletica.backend.repository.ProductRepository;
import com.athletica.backend.service.OrderService;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;
  private final ProductRepository productRepository;
  private final AddressRepository addressRepository;
  private final OrderMapper orderMapper;

  public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository,
      AddressRepository addressRepository, OrderMapper orderMapper) {
    this.orderRepository = orderRepository;
    this.productRepository = productRepository;
    this.addressRepository = addressRepository;
    this.orderMapper = orderMapper;
  }

  @Override
  @Transactional
  public OrderDto createOrder(CreateOrderRequest request) {
    // Validaciones mínimas
    if (request == null || request.getItems() == null || request.getItems().isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El pedido debe contener al menos un item");
    }
    if (request.getUserId() == null || request.getUserId().isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userId es requerido");
    }
    if (request.getAddressId() == null || request.getAddressId().isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "addressId es requerido");
    }

    Address address = addressRepository.findById(request.getAddressId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dirección no encontrada"));

    // Construir entidad Order
    Order order = new Order();
    order.setId(UUID.randomUUID().toString());

    User userRef = new User();
    userRef.setId(request.getUserId());
    order.setUser(userRef);

    order.setAddress(address);
    order.setStatus(OrderStatus.CREATED);
    order.setCreatedAt(LocalDateTime.now());
    order.setUpdatedAt(LocalDateTime.now());

    List<OrderItem> items = new ArrayList<>();
    BigDecimal subtotal = BigDecimal.ZERO;

    // Recorremos items y reservamos/decrementamos stock
    for (CreateOrderRequest.OrderItemRequest itemReq : request.getItems()) {
      String productId = itemReq.getProductId();
      Integer qty = itemReq.getQuantity();
      if (productId == null || productId.isBlank() || qty == null || qty <= 0) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
            "ProductId y quantity válidos son requeridos para cada item");
      }

      Product product = productRepository.findById(productId)
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado: " + productId));

      // Verificar stock antes de crear item
      if (product.getStock() < qty) {
        throw new ResponseStatusException(HttpStatus.CONFLICT,
            "Stock insuficiente para productId: " + productId + " (disponible: " + product.getStock() + ")");
      }

      // Intento de decrementar stock de forma atómica (ajusta el nombre del método
      // según tu repo)
      int updated = productRepository.decrementStock(product.getId(), qty);
      if (updated == 0) {
        // otro proceso pudo consumir el stock entre la verificación y la actualización
        throw new ResponseStatusException(HttpStatus.CONFLICT,
            "No fue posible reservar stock para productId: " + productId);
      }

      BigDecimal unitPrice = product.getPrice();
      BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(qty));

      OrderItem oi = new OrderItem();
      oi.setId(UUID.randomUUID().toString());
      oi.setOrder(order);
      oi.setProduct(product);
      oi.setQuantity(qty);
      oi.setUnitPrice(unitPrice);
      oi.setLineTotal(lineTotal);
      oi.setCreatedAt(LocalDateTime.now());

      items.add(oi);
      subtotal = subtotal.add(lineTotal);
    }

    order.setItems(items);
    order.setSubtotal(subtotal);

    BigDecimal tax = calculateTax(subtotal);
    BigDecimal shipping = calculateShipping(subtotal, address);

    order.setTax(tax);
    order.setShipping(shipping);
    order.setTotal(subtotal.add(tax).add(shipping));

    Order saved = orderRepository.save(order);
    return orderMapper.toDto(saved);
  }

  @Override
  @Transactional(readOnly = true)
  public List<OrderDto> getAllOrders() {
    return orderRepository.findAll()
        .stream()
        .map(orderMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<OrderDto> getOrdersForUser(String userId) {
    if (userId == null || userId.isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userId es requerido");
    }
    // asume que OrderRepository tiene findByUserId
    return orderRepository.findByUserId(userId)
        .stream()
        .map(orderMapper::toDto)
        .collect(Collectors.toList());
  }

  // Ejemplo simple: 10% de impuesto
  private BigDecimal calculateTax(BigDecimal subtotal) {
    if (subtotal == null)
      return BigDecimal.ZERO;
    return subtotal.multiply(BigDecimal.valueOf(0.10)).setScale(2, BigDecimal.ROUND_HALF_UP);
  }

  // Ejemplo simple: envío gratis sobre 100, sino 10
  private BigDecimal calculateShipping(BigDecimal subtotal, Address address) {
    if (subtotal == null)
      return BigDecimal.ZERO;
    BigDecimal freeThreshold = BigDecimal.valueOf(100);
    if (subtotal.compareTo(freeThreshold) >= 0)
      return BigDecimal.ZERO;
    return BigDecimal.valueOf(10).setScale(2);
  }
}
