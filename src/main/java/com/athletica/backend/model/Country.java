package com.athletica.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "countries")

public class Country {
  @Id
  @Column(length = 36)
  private String id;

  @Column(nullable = false, unique = true)
  private String name;

  @Column(name = "iso_code", length = 3)
  private String isoCode;

  public Country() {
  }

  public Country(String id, String name, String isoCode) {
    this.id = id;
    this.name = name;
    this.isoCode = isoCode;
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

  public String getIsoCode() {
    return isoCode;
  }

  public void setIsoCode(String isoCode) {
    this.isoCode = isoCode;
  }

}
