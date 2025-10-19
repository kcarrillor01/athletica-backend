package com.athletica.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.athletica.backend.model.Role;

public interface RoleRepository extends JpaRepository<Role, Short> {
  Role findByName(String name);
}
