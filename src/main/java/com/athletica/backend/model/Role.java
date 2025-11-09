package com.athletica.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles")

public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Short id; // tinyint unsigned fits in short

  @Column(nullable = false, unique = true)
  private String name;

  private String description;

  public Role() {
  }

  public Role(Short id, String name, String description) {
    this.id = id;
    this.name = name;
    this.description = description;
  }

  public Short getId() {
    return id;
  }

  public void setId(Short id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

}
