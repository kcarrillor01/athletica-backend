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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "cities", uniqueConstraints = @UniqueConstraint(columnNames = { "country_id",
    "name" }, name = "uq_city_country_name"))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class City {
  @Id
  @Column(length = 36)
  private String id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "country_id", nullable = false, foreignKey = @ForeignKey(name = "fk_city_country"))
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private Country country;

  @Column(nullable = false)
  private String name;

  @Column(name = "postal_prefix")
  private String postalPrefix;
}
