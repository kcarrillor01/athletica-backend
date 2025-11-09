package com.athletica.backend.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athletica.backend.model.Role;
import com.athletica.backend.repository.RoleRepository;
import com.athletica.backend.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

  @Autowired
  private RoleRepository roleRepository;

  @Override
  public List<Role> getAllRoles() {
    return roleRepository.findAll();
  }
}
