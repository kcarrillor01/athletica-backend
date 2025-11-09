package com.athletica.backend.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;

public class CategoryDto {
  private String id;

  @NotBlank(message = "El nombre es obligatorio")
  private String name;

  @NotBlank(message = "El slug es obligatorio")
  private String slug;

  private String description;
  private LocalDateTime createdAt;

  public CategoryDto() {
  }

  public CategoryDto(String id, String name, String slug, String description, LocalDateTime createdAt) {
    this.id = id;
    this.name = name;
    this.slug = slug;
    this.description = description;
    this.createdAt = createdAt;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSlug() {
    return slug;
  }

  public void setSlug(String slug) {
    this.slug = slug;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }
}
