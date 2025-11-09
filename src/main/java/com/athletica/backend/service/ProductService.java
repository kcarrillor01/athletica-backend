package com.athletica.backend.service;

import java.util.List;

import com.athletica.backend.dto.ProductDto;

public interface ProductService {
  List<ProductDto> getAllProducts();

  ProductDto getProduct(String id);

  List<ProductDto> filterProducts(String title, List<String> categories, Boolean inStock, Boolean active);

  ProductDto saveProduct(ProductDto productDto); // crea o edita según productDto.id

  void deleteProduct(String id);
}