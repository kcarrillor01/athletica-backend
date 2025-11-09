package com.athletica.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.athletica.backend.model.Address;

public interface AddressRepository extends JpaRepository<Address, String> {
  List<Address> findAllByUserId(String userId);
}
