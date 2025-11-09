package com.athletica.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.athletica.backend.dto.ApiResponse;
import com.athletica.backend.dto.ProductDto;
import com.athletica.backend.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

  private final ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<ProductDto>>> getAllProducts() {
    List<ProductDto> products = productService.getAllProducts();
    ApiResponse<List<ProductDto>> res = new ApiResponse<>(200, "OK", products);
    return ResponseEntity.ok(res);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<ProductDto>> getProduct(@PathVariable String id) {
    ProductDto dto = productService.getProduct(id);
    ApiResponse<ProductDto> res = new ApiResponse<>(200, "OK", dto);
    return ResponseEntity.ok(res);
  }

  @GetMapping("/search")
  public ResponseEntity<ApiResponse<List<ProductDto>>> filterProducts(
      @RequestParam(required = false) String title,
      @RequestParam(required = false) List<String> categories,
      @RequestParam(required = false) Boolean inStock,
      @RequestParam(required = false) Boolean active) {
    List<ProductDto> filtered = productService.filterProducts(title, categories, inStock, active);
    return ResponseEntity.ok(new ApiResponse<>(200, "OK", filtered));
  }

  @PostMapping
  public ResponseEntity<ApiResponse<ProductDto>> postProduct(@RequestBody ProductDto dto) {
    ProductDto saved = productService.saveProduct(dto);
    ApiResponse<ProductDto> res = new ApiResponse<>(200, "OK", saved);
    return ResponseEntity.ok(res);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<Object>> deleteProduct(@PathVariable String id) {
    productService.deleteProduct(id);
    ApiResponse<Object> res = new ApiResponse<>(200, "Deleted", null);
    return ResponseEntity.ok(res);
  }
}
