package com.athletica.backend.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.athletica.backend.dto.ProductDto;
import com.athletica.backend.model.Category;
import com.athletica.backend.model.Product;
import com.athletica.backend.repository.CategoryRepository;
import com.athletica.backend.repository.ProductRepository;
import com.athletica.backend.service.ProductService;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;

  // Constructor explícito (inyección por constructor)
  public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
    this.productRepository = productRepository;
    this.categoryRepository = categoryRepository;
  }

  @Override
  public List<ProductDto> getAllProducts() {
    // Si productRepository tiene findByActiveTrue, lo usará; si no, usa findAll()
    try {
      return productRepository.findByActiveTrue()
          .stream()
          .map(this::toDto)
          .collect(Collectors.toList());
    } catch (NoSuchMethodError | NoClassDefFoundError e) {
      return productRepository.findAll()
          .stream()
          .map(this::toDto)
          .collect(Collectors.toList());
    }
  }

  @Override
  public ProductDto getProduct(String id) {
    return productRepository.findById(id)
        .map(this::toDto)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
  }

  @Override
  public List<ProductDto> filterProducts(String title, List<String> categories, Boolean inStock, Boolean active) {
    List<Product> products = productRepository.findAll();

    return products.stream()
        .filter(p -> title == null || p.getTitle().toLowerCase().contains(title.toLowerCase()))
        .filter(p -> categories == null || categories.isEmpty() ||
            (p.getCategory() != null && categories.contains(p.getCategory().getId())))
        .filter(p -> inStock == null || (inStock ? p.getStock() > 0 : p.getStock() <= 0))
        .filter(p -> active == null || p.getActive().equals(active))
        .map(this::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public ProductDto saveProduct(ProductDto productDto) {
    boolean creating = (productDto.getId() == null || productDto.getId().isBlank());
    Product product;
    try {
      if (creating) {
        product = new Product();
        product.setId(UUID.randomUUID().toString());
        product.setCreatedAt(LocalDateTime.now());
        product.setActive(productDto.getActive() == null ? true : productDto.getActive());
      } else {
        product = productRepository.findById(productDto.getId())
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado para actualizar"));
      }

      // Asignar campos comunes
      product.setTitle(productDto.getTitle());
      product.setDescription(productDto.getDescription());
      product.setImage(productDto.getImage());
      product.setPrice(productDto.getPrice());
      product.setStock(productDto.getStock());
      product.setUpdatedAt(LocalDateTime.now());
      if (productDto.getActive() != null) {
        product.setActive(productDto.getActive());
      }

      // Vincular categoría si se proporcionó
      if (productDto.getCategoryId() != null && !productDto.getCategoryId().isBlank()) {
        Category cat = categoryRepository.findById(productDto.getCategoryId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria no encontrada"));
        product.setCategory(cat);
      } else {
        product.setCategory(null);
      }

      Product saved = productRepository.save(product);
      return toDto(saved);
    } catch (org.springframework.dao.DataIntegrityViolationException ex) {
      // Ejemplo: unique constraint violation
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Violación de restricción en base de datos");
    } catch (Exception ex) {
      // cualquier otro error inesperado
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno");
    }
  }

  @Override
  public void deleteProduct(String id) {
    if (!productRepository.existsById(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado");
    }
    productRepository.deleteById(id);
  }

  /* ---------------------- Helpers ---------------------- */

  private ProductDto toDto(Product p) {
    if (p == null) {
      return null;
    }
    ProductDto dto = new ProductDto();
    dto.setId(p.getId());
    dto.setTitle(p.getTitle());
    dto.setDescription(p.getDescription());
    dto.setImage(p.getImage());
    dto.setPrice(p.getPrice());
    dto.setStock(p.getStock());
    dto.setCategoryId(p.getCategory() != null ? p.getCategory().getId() : null);
    dto.setActive(p.getActive());
    dto.setCreatedAt(p.getCreatedAt());
    dto.setUpdatedAt(p.getUpdatedAt());
    return dto;
  }
}
