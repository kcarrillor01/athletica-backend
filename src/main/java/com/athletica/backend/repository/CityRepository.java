package com.athletica.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.athletica.backend.model.City;

public interface CityRepository extends JpaRepository<City, String> {

  /**
   * Todas las ciudades de un país (usa country.id)
   */
  List<City> findByCountryId(String countryId);

  /**
   * Buscar por nombre (coincidencia parcial, case-insensitive)
   */
  List<City> findByNameContainingIgnoreCase(String name);

  /**
   * Existe ciudad con (countryId, name) — útil para evitar duplicados al importar
   */
  boolean existsByCountryIdAndName(String countryId, String name);

  /**
   * Buscar ciudad por nombre exacto y país
   */
  Optional<City> findByNameAndCountryId(String name, String countryId);

  /**
   * Ejemplo de consulta JPA personalizada: buscar ciudades por prefijo postal (si
   * lo llenas)
   */
  @Query("SELECT c FROM City c WHERE c.postalPrefix = ?1")
  List<City> findByPostalPrefix(String postalPrefix);
}
