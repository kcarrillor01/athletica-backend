package com.athletica.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class ProductDto {

  private String id;

  @NotBlank(message = "El título es obligatorio")
  private String title;

  @NotBlank(message = "La descripción es obligatoria")
  private String description;

  private String image;

  @NotNull(message = "El precio es obligatorio")
  @PositiveOrZero(message = "El precio no puede ser negativo")
  private BigDecimal price;

  @NotNull(message = "El stock es obligatorio")
  @PositiveOrZero(message = "El stock no puede ser negativo")
  private Integer stock;

  @NotBlank(message = "La categoría es obligatoria")
  private String categoryId;

  private Boolean active = true; // Valor por defecto

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public ProductDto() {
    this.active = true; // refuerzo del valor por defecto
  }

  // Constructor completo
  public ProductDto(String id, String title, String description, String image, BigDecimal price, Integer stock,
      String categoryId, Boolean active, LocalDateTime createdAt, LocalDateTime updatedAt) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.image = image;
    this.price = price;
    this.stock = stock;
    this.categoryId = categoryId;
    this.active = (active != null) ? active : true;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  // Getters y Setters
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public Integer getStock() {
    return stock;
  }

  public void setStock(Integer stock) {
    this.stock = stock;
  }

  public String getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(String categoryId) {
    this.categoryId = categoryId;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = (active != null) ? active : true;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }
}
