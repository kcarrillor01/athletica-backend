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
@Entity
@Table(name = "addresses")
public class Address {
  @Id
  @Column(length = 36)
  private String id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_address_user"))

  private User user;

  private String label;

  @Column(name = "address_line1", nullable = false)
  private String addressLine1;

  @Column(name = "address_line2")
  private String addressLine2;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "city_id", nullable = false, foreignKey = @ForeignKey(name = "fk_address_city"))
  private City city;

  private String state;

  @Column(name = "postal_code")
  private String postalCode;

  private String phone;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  public Address() {
  }

  public Address(String id, User user, String label, String addressLine1, String addressLine2, City city, String state,
      String postalCode, String phone, LocalDateTime createdAt) {
    this.id = id;
    this.user = user;
    this.label = label;
    this.addressLine1 = addressLine1;
    this.addressLine2 = addressLine2;
    this.city = city;
    this.state = state;
    this.postalCode = postalCode;
    this.phone = phone;
    this.createdAt = createdAt;
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

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getAddressLine1() {
    return addressLine1;
  }

  public void setAddressLine1(String addressLine1) {
    this.addressLine1 = addressLine1;
  }

  public String getAddressLine2() {
    return addressLine2;
  }

  public void setAddressLine2(String addressLine2) {
    this.addressLine2 = addressLine2;
  }

  public City getCity() {
    return city;
  }

  public void setCity(City city) {
    this.city = city;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  
}
