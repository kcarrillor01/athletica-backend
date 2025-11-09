package com.athletica.backend.model;

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
@Table(name = "cities", uniqueConstraints = @UniqueConstraint(columnNames = { "country_id",
    "name" }, name = "uq_city_country_name"))

public class City {
  @Id
  @Column(length = 36)
  private String id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "country_id", nullable = false, foreignKey = @ForeignKey(name = "fk_city_country"))
  private Country country;

  @Column(nullable = false)
  private String name;

  @Column(name = "postal_prefix")
  private String postalPrefix;

  public City() {
  }

  public City(String id, Country country, String name, String postalPrefix) {
    this.id = id;
    this.country = country;
    this.name = name;
    this.postalPrefix = postalPrefix;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Country getCountry() {
    return country;
  }

  public void setCountry(Country country) {
    this.country = country;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPostalPrefix() {
    return postalPrefix;
  }

  public void setPostalPrefix(String postalPrefix) {
    this.postalPrefix = postalPrefix;
  }

}
