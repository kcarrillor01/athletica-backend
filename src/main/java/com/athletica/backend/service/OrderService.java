package com.athletica.backend.service;

import org.springframework.stereotype.Service;

import com.athletica.backend.repository.OrderRepository;
import com.athletica.backend.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
  private final ProductRepository productRepository;
  private final OrderRepository orderRepository;

  
}
