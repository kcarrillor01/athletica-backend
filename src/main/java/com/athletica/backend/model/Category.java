package com.athletica.backend.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "categories", uniqueConstraints = @UniqueConstraint(columnNames = "slug"))

public class Category {
  @Id
  @Column(length = 36)
  private String id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String slug;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  public Category() {
  }

  public Category(String id, String name, String slug, String description, LocalDateTime createdAt) {
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
