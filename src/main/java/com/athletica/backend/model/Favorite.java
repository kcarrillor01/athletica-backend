package com.athletica.backend.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "favorites", uniqueConstraints = @UniqueConstraint(columnNames = { "user_id",
    "product_id" }, name = "uq_fav_user_product"))

public class Favorite {
  @Id
  @Column(length = 36)
  private String id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_fav_user"))
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_fav_product"))
  private Product product;

  @Column(name = "added_at", nullable = false)
  private LocalDateTime addedAt;

  public Favorite() {
  }

  public Favorite(String id, User user, Product product, LocalDateTime addedAt) {
    this.id = id;
    this.user = user;
    this.product = product;
    this.addedAt = addedAt;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public LocalDateTime getAddedAt() {
    return addedAt;
  }

  public void setAddedAt(LocalDateTime addedAt) {
    this.addedAt = addedAt;
  }

}
